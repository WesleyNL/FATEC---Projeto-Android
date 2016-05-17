package br.com.app.activity.chat.pesquisa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.app.Sistema;
import br.com.app.activity.R;
import br.com.app.activity.chat.resultados.ChatResultadosActivity;

import br.com.app.business.app.configuracao.ConfiguracaoDAO;
import br.com.app.enums.EnmTelas;
import br.com.app.utils.IdiomaFluencia;
import br.com.app.utils.Utils;
import br.com.app.business.app.login.LoginDAO;
import br.com.app.business.chat.pesquisa.PesquisaDAO;

import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jefferson on 31/03/2016.
 */
public class ChatPesquisaActivity extends Activity {

    private TextView lblDistanciaSelecionada = null;
    private SeekBar skrDistancia = null;
    private int distanciaSelecionada = 0;

    private PesquisaDAO objProcDAO = null;
    private LoginDAO objLoginDAO = null;

    private final int ACCESS_COARSE_LOCATION = 0;
    private LocationManager locationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_pesquisa);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initComponentes();

        if (hasLocationPermission()) {
            salvar();
        }

        verificarConfiguracao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLocalizacao();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void atualizarLocalizacao(){
        if(locationManager != null){
            if(getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getPackageName()) == getPackageManager().PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener)new Utils());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return (true);
    }

    @Override
    public boolean onMenuItemSelected(int panel, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itmConfiguracoes:
                Utils.chamarActivity(this, EnmTelas.APP_CONFIGURACOES);
                break;

            case R.id.itmSobre:
                Utils.chamarActivity(this, EnmTelas.APP_SOBRE);
                break;

            case R.id.itmSair:
                encerrar();
                break;
        }

        return (true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                salvar();
            }
            else {
                Toast.makeText(this, getString(R.string.necessario_permitir), Toast.LENGTH_SHORT).show();
                encerrar();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasLocationPermission() {
        int status = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_COARSE_LOCATION);
                return false;
            }
        }
        else {
            status = getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getPackageName());

            if (status != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    private void initComponentes() {

        carregarComboIdioma();
        carregarComboFluencia();

        skrDistancia = (SeekBar) findViewById(R.id.skrDistancia);
        lblDistanciaSelecionada = (TextView) findViewById(R.id.lblDistanciaSelecionada);

        skrDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                lblDistanciaSelecionada.setText(progressValue + " Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distanciaSelecionada = skrDistancia.getProgress();
            }
        });

        objProcDAO = new PesquisaDAO();

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    }

    public void salvar() {

        objLoginDAO = new LoginDAO();
        objLoginDAO.setUserId(Sistema.USER_ID);
        objLoginDAO.setCoordUltimoAcesso(Utils.getCoordenadas(this));

        if (!objLoginDAO.salvar()) {
            Toast.makeText(this, getString(R.string.indisponivel_sistema), Toast.LENGTH_LONG).show();
            encerrar();
        }
    }

    public void carregarComboIdioma(){
        Spinner cmbIdioma = (Spinner) findViewById(R.id.cmbIdiomaProcurar);
        Utils.carregarComboIdiomas(cmbIdioma, this, true);
        cmbIdioma.setSelection(Utils.getPosicaoIdioma(1));
    }

    public void carregarComboFluencia(){
        Spinner cmbFluencia = (Spinner) findViewById(R.id.cmbFluenciaProcurar);
        Utils.carregarComboFluencia(cmbFluencia, this, true);
        cmbFluencia.setSelection(Utils.getPosicaoFluencia(1));
    }

    public void procurar(View view){

        Spinner cmbIdioma = (Spinner) findViewById(R.id.cmbIdiomaProcurar);
        Spinner cmbFluencia = (Spinner) findViewById(R.id.cmbFluenciaProcurar);
        IdiomaFluencia idiomaFluencia = null;

        objProcDAO.setUserId(Sistema.USER_ID);
        idiomaFluencia = (IdiomaFluencia)cmbIdioma.getItemAtPosition(cmbIdioma.getSelectedItemPosition());
        objProcDAO.setIdioma((byte)idiomaFluencia.getId());
        idiomaFluencia = (IdiomaFluencia)cmbFluencia.getItemAtPosition(cmbFluencia.getSelectedItemPosition());
        objProcDAO.setFluencia((byte)idiomaFluencia.getId());
        objProcDAO.setDistancia(distanciaSelecionada);

        if (objProcDAO.procurar()){

            Bundle bundle = new Bundle();
            bundle.putSerializable("listaUsuarios", objProcDAO.getListaUsuarios());

            Intent i = new Intent(this, ChatResultadosActivity.class);
            i.putExtra("listaUsuarios", bundle);
            startActivity(i);
        }
        else{
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(getString(R.string.resultado));
            dialogo.setMessage(getString(R.string.usuarios_nao_encontrados));
            dialogo.setNeutralButton(getString(R.string.ok), null);
            dialogo.show();
        }
    }

    public void verificarConfiguracao(){

        ConfiguracaoDAO objConfDAO = new ConfiguracaoDAO();
        objConfDAO.setUserId(Sistema.USER_ID);

        if(!objConfDAO.existe()){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

            dialogo.setTitle(getString(R.string.configurar));
            dialogo.setMessage(getString(R.string.configuracoes_nao_personalizadas) + "\n" + getString(R.string.fazer_agora));
            dialogo.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Utils.chamarActivity(ChatPesquisaActivity.this, EnmTelas.APP_CONFIGURACOES);
                }
            });
            dialogo.setNegativeButton(getString(R.string.nao), null);
            dialogo.show();
        }
    }

    private void encerrar() {
        Utils.chamarActivity(this, EnmTelas.APP_LOGIN, "LOGOUT", true);
    }
}
