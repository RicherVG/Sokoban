package sokobanrg.servicio;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ManejadorRecursos {
    private BitmapFont fuentePrincipal;
    private BitmapFont fuenteTitulo;
    private Skin skinUI;

    public ManejadorRecursos() {
    }

    public void cargarRecursos() {
        generarFuentesYSkin();
    }

    private void generarFuentesYSkin() {
        fuentePrincipal = new BitmapFont();
        fuentePrincipal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuentePrincipal.getData().setScale(1.4f);
        fuentePrincipal.setColor(Color.WHITE);

        fuenteTitulo = new BitmapFont();
        fuenteTitulo.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuenteTitulo.getData().setScale(3.2f);

        BitmapFont fuenteSubtitulo = new BitmapFont();
        fuenteSubtitulo.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuenteSubtitulo.getData().setScale(1.7f);

        BitmapFont fuenteBoton = new BitmapFont();
        fuenteBoton.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fuenteBoton.getData().setScale(1.5f);

        skinUI = new Skin();
        skinUI.add("default-font",  fuentePrincipal);
        skinUI.add("title-font",    fuenteTitulo);
        skinUI.add("subtitle-font", fuenteSubtitulo);
        skinUI.add("button-font",   fuenteBoton);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        Texture texBlanco = new Texture(pm);
        skinUI.add("white", texBlanco);
        pm.dispose();

        Color colPanelFondo     = new Color(0.08f, 0.08f, 0.18f, 0.92f);
        Color colBorde          = new Color(0.85f, 0.65f, 0.10f, 1f);
        Color colBotonUp        = new Color(0.80f, 0.55f, 0.05f, 1f);
        Color colBotonOver      = new Color(0.95f, 0.70f, 0.15f, 1f);
        Color colBotonDown      = new Color(0.60f, 0.38f, 0.02f, 1f);
        Color colSecUp          = new Color(0.18f, 0.22f, 0.35f, 1f);
        Color colSecOver        = new Color(0.26f, 0.32f, 0.50f, 1f);
        Color colSecDown        = new Color(0.10f, 0.13f, 0.22f, 1f);
        Color colCampoFondo     = new Color(0.06f, 0.08f, 0.20f, 1f);
        Color colCampoSeleccion = new Color(0.85f, 0.65f, 0.10f, 0.4f);
        Color colGris           = new Color(0.3f, 0.3f, 0.3f, 1f);

        skinUI.add("panel-bg",  new TextureRegionDrawable(texBlanco).tint(colPanelFondo),  Drawable.class);
        skinUI.add("border-bg", new TextureRegionDrawable(texBlanco).tint(colBorde),         Drawable.class);

        TextButtonStyle estiloBoton = new TextButtonStyle();
        estiloBoton.up       = new TextureRegionDrawable(texBlanco).tint(colBotonUp);
        estiloBoton.over     = new TextureRegionDrawable(texBlanco).tint(colBotonOver);
        estiloBoton.down     = new TextureRegionDrawable(texBlanco).tint(colBotonDown);
        estiloBoton.disabled = new TextureRegionDrawable(texBlanco).tint(colGris);
        estiloBoton.font      = fuenteBoton;
        estiloBoton.fontColor = new Color(0.08f, 0.05f, 0.00f, 1f);
        skinUI.add("default", estiloBoton);

        TextButtonStyle estiloBotonSec = new TextButtonStyle();
        estiloBotonSec.up       = new TextureRegionDrawable(texBlanco).tint(colSecUp);
        estiloBotonSec.over     = new TextureRegionDrawable(texBlanco).tint(colSecOver);
        estiloBotonSec.down     = new TextureRegionDrawable(texBlanco).tint(colSecDown);
        estiloBotonSec.disabled = new TextureRegionDrawable(texBlanco).tint(colGris);
        estiloBotonSec.font      = fuenteBoton;
        estiloBotonSec.fontColor = new Color(0.75f, 0.80f, 1f, 1f);
        skinUI.add("secondary", estiloBotonSec);

        LabelStyle estiloTitulo = new LabelStyle();
        estiloTitulo.font      = fuenteTitulo;
        estiloTitulo.fontColor = new Color(0.95f, 0.78f, 0.15f, 1f);
        skinUI.add("title", estiloTitulo);

        LabelStyle estiloSubtitulo = new LabelStyle();
        estiloSubtitulo.font      = fuenteSubtitulo;
        estiloSubtitulo.fontColor = new Color(0.75f, 0.80f, 1.00f, 1f);
        skinUI.add("subtitle", estiloSubtitulo);

        LabelStyle estiloDefault = new LabelStyle();
        estiloDefault.font      = fuentePrincipal;
        estiloDefault.fontColor = Color.WHITE;
        skinUI.add("default", estiloDefault);

        LabelStyle estiloMensaje = new LabelStyle();
        estiloMensaje.font      = fuentePrincipal;
        estiloMensaje.fontColor = new Color(1f, 0.55f, 0.15f, 1f);
        skinUI.add("message", estiloMensaje);

        TextFieldStyle estiloTextField = new TextFieldStyle();
        estiloTextField.font             = fuentePrincipal;
        estiloTextField.fontColor        = Color.WHITE;
        estiloTextField.messageFontColor = new Color(0.5f, 0.55f, 0.65f, 1f);
        estiloTextField.background       = new TextureRegionDrawable(texBlanco).tint(colCampoFondo);
        estiloTextField.focusedBackground = new TextureRegionDrawable(texBlanco).tint(new Color(0.10f, 0.12f, 0.30f, 1f));
        estiloTextField.cursor           = new TextureRegionDrawable(texBlanco).tint(colBorde);
        estiloTextField.selection        = new TextureRegionDrawable(texBlanco).tint(colCampoSeleccion);
        skinUI.add("default", estiloTextField);
    }

    public Skin getSkinUI() {
        return skinUI;
    }

    public void liberarRecursos() {
        if (fuentePrincipal != null) {
            fuentePrincipal.dispose();
        }
        if (fuenteTitulo != null) {
            fuenteTitulo.dispose();
        }
        if (skinUI != null) {
            skinUI.dispose();
        }
    }
}
