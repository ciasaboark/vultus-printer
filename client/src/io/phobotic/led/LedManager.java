package io.phobotic.led;

import com.pi4j.io.gpio.*;

/**
 * Created by Jonathan Nelson on 7/30/18.
 */
public class LedManager {
    private static LedManager instance;
    private GpioController gpio;
    private GpioPinDigitalOutput powerLED;
    private GpioPinDigitalOutput statusLED;
    private GpioPinDigitalOutput internalLED;
    private boolean initialized = false;

    private LedManager() {
        initialized = false;

        try {
            gpio = GpioFactory.getInstance();

            //initialize all of the LEDs
            powerLED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Power LED", PinState.LOW);
            statusLED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Status LED", PinState.LOW);
            internalLED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Internal LED", PinState.LOW);
            initialized = true;
        } catch (NoClassDefFoundError e) {
            System.err.println("Unable to load GPIO library.  LEDs will be disabled.");
        } catch (UnsatisfiedLinkError e) {
            //if the GPIO library failed to load just skip using the LEDS
            System.err.println("Unable to load GPIO library.  LEDs will be disabled.");
        } catch (Exception e) {
            System.err.println("Caught exception initializing Raspberry PI GPIO LEDs.  All LEDs will be disabled.");
        }
    }

    public static LedManager getInstance() {
        if (instance == null) {
            instance = new LedManager();
        }

        return instance;
    }

    public void fastFlashLeds(int flashes, Led... leds) {
        flashLeds(200, flashes, leds);
    }

    public void slowFlashLeds(int flashes, Led... leds) {
        flashLeds(800, flashes, leds);
    }

    private void flashLeds(long sleep, int flashes, Led[] leds) {
        if (leds != null && initialized) {
            //turn off each LED
            for (Led led: leds) {
                turnOffLed(led);
            }


            for (int i = 0; i < flashes; i++) {
                for (Led led: leds) {
                    turnOnLed(led);
                }

                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Led led: leds) {
                    turnOffLed(led);
                }
            }
        }
    }

    private void turnOffLed(Led led) {
        GpioPinDigitalOutput pin = null;
        switch (led) {
            case POWER:
                pin = powerLED;
                break;
            case STATUS:
                pin = statusLED;
                break;
            case INTERNAL:
                pin = internalLED;
                break;
        }

        if (led != null){
            pin.low();
        }
    }

    private void turnOnLed(Led led) {
        GpioPinDigitalOutput pin = null;
        switch (led) {
            case POWER:
                pin = powerLED;
                break;
            case STATUS:
                pin = statusLED;
                break;
            case INTERNAL:
                pin = internalLED;
                break;
        }

        if (led != null){
            pin.high();
        }
    }


    public enum Led {
        POWER,
        STATUS,
        INTERNAL
    }


}
