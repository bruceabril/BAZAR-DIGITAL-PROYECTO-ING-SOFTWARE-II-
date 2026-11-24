package com.bazardigital;

import com.bazardigital.ui.console.AplicacionConsola;
import com.bazardigital.ui.gui.AplicacionGrafica;

public final class BazarDigitalApp {
    private BazarDigitalApp() {
    }

    public static void main(String[] args) {
        if (args.length > 0 && "--gui".equalsIgnoreCase(args[0])) {
            AplicacionGrafica.iniciar();
        } else {
            AplicacionConsola.iniciar();
        }
    }
}
