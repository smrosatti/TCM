/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.MediasDao;
import Main.MAtualizar;
import Main.MEditUser;
import Main.MPrincipal;
import Model.Medias;
import Model.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SARA
 */
public class TelaAtualizarController implements Initializable {

    public static Usuario getLogado() {
        return logado;
    }

    public static void setLogado(Usuario aLogado) {
        logado = aLogado;
    }

    @FXML
    private DatePicker dataleitura;

    @FXML
    private Button btvoltar;

    @FXML
    private Button btsalvar;

    @FXML
    private TextField num1;

    @FXML
    private TextField litrosf;

    @FXML
    private TextField reaisf;

    @FXML
    private TextField num2;

    @FXML
    private Label nomeuser;

    @FXML
    private ImageView imguser;

    @FXML
    private Button btlimpar;

    private static Usuario logado;

    private static Medias med;

    private static Medias select;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializar();

        btsalvar.setOnMouseClicked((MouseEvent evt) -> {
            salvar();
        });

        btvoltar.setOnMouseClicked((MouseEvent evt) -> {
            cancel();
        });

        btlimpar.setOnMouseClicked((MouseEvent evt) -> {
            limpar();
        });
    }

    public void limpar() {
        num1.clear();
        num2.clear();
        dataleitura.setValue(null);
        litrosf.clear();
        reaisf.clear();
    }

    public void btperfil() {
        try {
            MEditUser tela = new MEditUser(logado);
            tela.start(new Stage());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static Float str2Float(String longStr) {
        try {
            return Float.parseFloat(longStr);
        } catch (Exception e) {
            return 0F;
        }
    }

    public void salvar() {
        try {

            if (num1.getText().trim().isEmpty() | num2.getText().trim().isEmpty() | dataleitura.toString().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Por favor preencha todos os campos!");
                a.show();

            } else {
                Medias m = new Medias();
                m.setRegistro1(Integer.valueOf(num1.getText()));
                m.setRegistro2(Integer.valueOf(num2.getText()));
                m.setData(dataleitura.getValue());
                m.setId_usuario(logado.getId_user());
                if (m.getRegistro1() == m.getRegistro2()) {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setHeaderText("Numeros adicionados com valores iguais, favor revisar os campos!");
                    al.showAndWait();
                } else if (m.getRegistro1() > m.getRegistro2()) {
                    Alert ae = new Alert(Alert.AlertType.ERROR);
                    ae.setHeaderText("Primeiro numero digitado é maior que o segundo, imporssivel calcular!");
                    ae.showAndWait();
                } else {
                    m.consumo_de_agua();

                    m.getRegisFinal();

                    MediasDao dao = new MediasDao();
                    dao.insereMedia(m);

                    Float val = (float) (m.getGasto());
                    String valStr = String.format("%06.2f%n", val);

                    reaisf.setText(valStr);
                    litrosf.setText(Double.toString(m.getRegisFinal()));

                    MPrincipal tela = new MPrincipal(logado);
                    tela.start(new Stage());

                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setHeaderText("Média Cadastrada com Sucesso!");
                    a.show();

                    MAtualizar.getStage().close();
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void teste() {
        Medias a = new Medias();
        a.setRegistro1(Integer.valueOf(num1.getText()));
        a.setRegistro2(Integer.valueOf(num2.getText()));
        a.consumo_de_agua();

        a.getRegisFinal();

        a.getGasto();
    }

    public void cancel() {
        try {
            MAtualizar.getStage().close();
            MPrincipal tela = new MPrincipal(logado);
            tela.start(new Stage());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void inicializar() {
        try {
            imguser.setImage(new Image(logado.getImagem()));
            nomeuser.setText(logado.getUser());
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

}
