import { definePreset } from '@primeng/themes';
import Aura from '@primeng/themes/aura';

export const MyPreset = definePreset(Aura, {
    semantic: {
        colorScheme: {
            light: {
                primary: {
                    50: '{amber.50}',
                    100: '{amber.100}',
                    200: '{amber.200}',
                    300: '{amber.300}',
                    400: '{amber.400}',
                    500: '{amber.500}',
                    600: '{amber.600}',
                    700: '{amber.700}',
                    800: '{amber.800}',
                    900: '{amber.900}',
                    950: '{amber.950}',
                },
                highlight: {
                    background: '{amber.700}',
                    focusBackground: '{amber.800}',
                    color: '#FAF3E0',
                    focusColor: '#FFFFFF',
                },
                secondary: {
                    50: '{blue.50}',
                    100: '{blue.100}',
                    200: '{blue.200}',
                    300: '{blue.300}',
                    400: '{blue.400}',
                    500: '{blue.500}',
                    600: '{blue.600}',
                    700: '{blue.700}',
                    800: '{blue.800}',
                    900: '{blue.900}',
                    950: '{blue.950}',
                },
                accent: {
                    50: '{teal.50}',
                    100: '{teal.100}',
                    200: '{teal.200}',
                    300: '{teal.300}',
                    400: '{teal.400}',
                    500: '{teal.500}',
                    600: '{teal.600}',
                    700: '{teal.700}',
                    800: '{teal.800}',
                    900: '{teal.900}',
                    950: '{teal.950}',
                },
                status: {
                    success: '{green.500}',
                    warning: '{yellow.500}',
                    error: '{red.500}',
                    info: '{blue.400}',
                },
                text: {
                    default: '{zinc.900}',  // Texto principal
                    secondary: '{zinc.700}', // Texto secundario
                    muted: '{zinc.500}',     // Texto más apagado
                    inverted: '{white}',     // Para fondos oscuros
                },
                /*custom: {
                    cardcolor: '{blue.500}',
                    background: '{amber.700}',
                },*/
            }
        }
    }
});
