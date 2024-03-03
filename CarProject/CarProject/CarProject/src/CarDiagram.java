import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import java.io.File;

public class CarDiagram extends JFrame
{
    private JPanel panel_main;
    private JButton button_start_engine;
    private JPanel panel_engine_status;
    private JButton button_stop_engine;
    private JTextField text_gas_tank;
    private JSlider slider_gas_pedal;
    private JTextField text_odometer;
    private JLabel label_gas_pedal;
    private JLabel label_gas_tank;
    private JLabel label_odometer;
    private JLabel label_break_pedal;
    private JButton button_brake_pedal;
    private JLabel label_speedometer;
    private JTextField text_speedometer;
    private JButton button_reset_odometer;
    private JPanel panel_position_status;
    private JPanel panel_dashboard_panel;
    private JPanel panel_direction_status;
    private JButton button_refill_tank;
    private JLabel label_coordinate_x;
    private JLabel label_coordinate_y;
    private JTextField text_x_coordinate;
    private JTextField text_y_coordinate;
    private JButton forwardButton;
    private JButton reverseButton;
    private JButton rightButton;
    private JButton leftButton;
    private JTextField text_engine_message;
    private JButton button_spanish_guitar;
    private JButton button_jazz;
    private JButton button_the_chuck;
    private JSlider slider_air_conditioner;
    private JSpinner spinner_air_flow;
    private JButton button_cold_air;
    private JButton button_hot_air;
    private JSlider slider_volume;
    private JSpinner spinner_volume;
    private JSpinner spinner_gas_pedal;
    private JLabel label_volume;
    private JPanel panel_radio;
    private JPanel panel_air_conditioner;
    private JPanel panel_radio_stations;
    private JLabel label_stations;
    private JButton button_turn_off;
    private JLabel label_decibel;
    private JTextField text_ac_status;
    private Timer timer;
    private boolean brake_pedal;
    private boolean filling_tank;

    //private boolean engine_status; // false = off, true = on
    //private boolean break_pedal; // false = off, true = on
    //private int wheel_status; // -100 to 100 for wheel status
    //private int gas_pedal; // 0 to 100 for gas pedal

    private String temp;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    Music music = new Music();
    String music_path;
    boolean hot_button;
    boolean cold_button;


    public CarDiagram() throws InterruptedException {
        brake_pedal = false;
        filling_tank = false;
        forwardButton.setEnabled(false);
        reverseButton.setEnabled(false);
        rightButton.setEnabled(false);
        leftButton.setEnabled(false);
        button_brake_pedal.setEnabled(false);
        button_reset_odometer.setEnabled(false);
        button_turn_off.setEnabled(false);
        hot_button = false;
        cold_button = false;

        button_start_engine.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean tankEmpty = isTankEmpty();

                if(!tankEmpty)
                {
                    text_engine_message.setText("");
                    button_stop_engine.setEnabled(true);
                    slider_gas_pedal.setEnabled(true);
                    button_start_engine.setEnabled(false);
                    button_reset_odometer.setEnabled(false);
                    button_refill_tank.setEnabled(false);
                    forwardButton.setEnabled(true);
                    reverseButton.setEnabled(true);
                    rightButton.setEnabled(true);
                    leftButton.setEnabled(true);
                    button_brake_pedal.setEnabled(true);
                    spinner_gas_pedal.setEnabled(true);
                }
                else
                {
                    text_engine_message.setText("REFILL GAS TANK!");
                }
            }
        });

        button_stop_engine.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                button_stop_engine.setEnabled(false);
                slider_gas_pedal.setEnabled(false);
                button_start_engine.setEnabled(true);
                button_reset_odometer.setEnabled(true);
                button_refill_tank.setEnabled(true);
                forwardButton.setEnabled(false);
                reverseButton.setEnabled(false);
                rightButton.setEnabled(false);
                leftButton.setEnabled(false);
                button_brake_pedal.setEnabled(false);
                text_speedometer.setText("0");
                slider_gas_pedal.setValue(0);
                spinner_gas_pedal.setEnabled(false);
            }
        });


        slider_gas_pedal.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(!brake_pedal)
                {
                    text_speedometer.setText(String.valueOf(slider_gas_pedal.getValue()));
                    spinner_gas_pedal.setValue(slider_gas_pedal.getValue());
                }
            }
        });

        spinner_gas_pedal.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int gas_pedal = (Integer) spinner_gas_pedal.getValue();

                if(gas_pedal > 100)
                {
                    spinner_gas_pedal.setValue(100);
                }

                if(gas_pedal < 0)
                {
                    spinner_gas_pedal.setValue(0);
                }

                slider_gas_pedal.setValue(gas_pedal);
            }
        });

        button_brake_pedal.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                brake_pedal = true;
                startCountDown();
            }
        });


        button_brake_pedal.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                super.mouseReleased(e);
                brake_pedal = false;
                stopCountDown();
            }
        });

        button_refill_tank.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                filling_tank = true;
                fillTank();
            }
        });

        button_refill_tank.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                super.mouseReleased(e);
                filling_tank = false;
            }
        });

        forwardButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int currentPosition = Integer.parseInt(text_y_coordinate.getText());
                text_y_coordinate.setText(String.valueOf(currentPosition + slider_gas_pedal.getValue()));
                text_odometer.setText(String.valueOf(Integer.parseInt(text_odometer.getText())
                                                                + slider_gas_pedal.getValue()));
                decreaseTank();
            }
        });

        reverseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int currentPosition = Integer.parseInt(text_y_coordinate.getText());
                text_y_coordinate.setText(String.valueOf(currentPosition - slider_gas_pedal.getValue()));
                text_odometer.setText(String.valueOf(Integer.parseInt(text_odometer.getText())
                                                                + slider_gas_pedal.getValue()));
                decreaseTank();
            }
        });

        rightButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int currentPosition = Integer.parseInt(text_x_coordinate.getText());
                text_x_coordinate.setText(String.valueOf(currentPosition + slider_gas_pedal.getValue()));
                text_odometer.setText(String.valueOf(Integer.parseInt(text_odometer.getText())
                                                                + slider_gas_pedal.getValue()));
                decreaseTank();
            }
        });

        leftButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int currentPosition = Integer.parseInt(text_x_coordinate.getText());
                text_x_coordinate.setText(String.valueOf(currentPosition - slider_gas_pedal.getValue()));
                text_odometer.setText(String.valueOf(Integer.parseInt(text_odometer.getText())
                                                                + slider_gas_pedal.getValue()));
                decreaseTank();
            }
        });

        button_reset_odometer.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                text_odometer.setText("0");
            }
        });

        slider_volume.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                spinner_volume.setValue((Integer) slider_volume.getValue()); //spinner_volume = slider_volume
                music.volume = slider_volume.getValue();
                music.change_volume();
            }
        });

        spinner_volume.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int volume = (Integer) spinner_volume.getValue(); // used for checking values greater than or less than

                if(volume > 80) // if volume is greater than 100
                {
                    spinner_volume.setValue(80); // set volume to 100
                }
                if(volume < -40) // if volume is less than 0
                {
                    spinner_volume.setValue(-40); // set volume to 0
                }

                slider_volume.setValue((Integer) spinner_volume.getValue()); //slider_volume = spinner_volume
            }
        });

        spinner_air_flow.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int air_flow = (Integer) spinner_air_flow.getValue();

                if(air_flow > 100) // if air flow is greater than 100
                {
                    spinner_air_flow.setValue(100); //set air flow to 100
                }

                if(air_flow < 0) // if air flow is less than 0
                {
                    spinner_air_flow.setValue(0); // set air flow to 0
                }

                slider_air_conditioner.setValue((Integer) spinner_air_flow.getValue());

            }
        });

        button_spanish_guitar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                button_spanish_guitar.setEnabled(false);
                button_jazz.setEnabled(false);
                button_the_chuck.setEnabled(false);
                button_turn_off.setEnabled(true);
                music_path = "CarProject/Isaac Albeniz - Asturias.wav";
                play_radio(music_path);
            }
        });

        button_jazz.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                button_spanish_guitar.setEnabled(false);
                button_jazz.setEnabled(false);
                button_the_chuck.setEnabled(false);
                button_turn_off.setEnabled(true);
                music_path = "CarProject/Cafe de Touhou - If the Sky Clears.wav";
                play_radio(music_path);
            }
        });

        button_the_chuck.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                button_spanish_guitar.setEnabled(false);
                button_jazz.setEnabled(false);
                button_the_chuck.setEnabled(false);
                button_turn_off.setEnabled(true);
                music_path = "CarProject/Chuck Mangione - Feels So Good.wav";
                play_radio(music_path);
            }
        });

        button_turn_off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                button_spanish_guitar.setEnabled(true);
                button_jazz.setEnabled(true);
                button_the_chuck.setEnabled(true);
                button_turn_off.setEnabled(false);
                music.stop();
            }
        });
        button_cold_air.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cold_button = !cold_button;
            }
        });

        button_hot_air.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                hot_button = !hot_button;
            }
        });

        slider_air_conditioner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                spinner_air_flow.setValue(slider_air_conditioner.getValue());
                if(cold_button && !hot_button)
                {
                    if(slider_air_conditioner.getValue() <= 20)
                    {
                        text_ac_status.setText("Nice cool breeze");
                    }

                    else if (20 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 40)
                    {
                        text_ac_status.setText("Nice and Chilly!");
                    }

                    else if (40 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 60)
                    {
                        text_ac_status.setText("Hope you packed something nice and warm!");
                    }

                    else if (60 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 80)
                    {
                        text_ac_status.setText("Brrrrrrrrr! Feels like the Artic!");
                    }

                    else if (80 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() < 100)
                    {
                        text_ac_status.setText("I cant feel my legs!");
                    }

                    else if (slider_air_conditioner.getValue() == 100)
                    {
                        text_ac_status.setText("I am now a popsicle!");
                    }
                }

                if(hot_button && !cold_button)
                {
                    if(slider_air_conditioner.getValue() <= 20)
                    {
                        text_ac_status.setText("Nice warm breeze");
                    }

                    else if (20 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 40)
                    {
                        text_ac_status.setText("Nice and warm!");
                    }

                    else if (40 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 60)
                    {
                        text_ac_status.setText("Is it hot in here, or is it me?!");
                    }

                    else if (60 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 80)
                    {
                        text_ac_status.setText("Simmer down hotshot!");
                    }

                    else if (80 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() < 100)
                    {
                        text_ac_status.setText("Do I smell bacon?");
                    }

                    else if (slider_air_conditioner.getValue() == 100)
                    {
                        text_ac_status.setText("My goose is cooked!");
                    }
                }

                if((cold_button && hot_button) || (!cold_button && !hot_button))
                {
                    if(slider_air_conditioner.getValue() <= 20)
                    {
                        text_ac_status.setText("Nice breeze?");
                    }

                    else if (20 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 40)
                    {
                        text_ac_status.setText("Moving Air!");
                    }

                    else if (40 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 60)
                    {
                        text_ac_status.setText("Did someone roll down a window?!");
                    }

                    else if (60 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() <= 80)
                    {
                        text_ac_status.setText("Man is it windy in the car!");
                    }

                    else if (80 < slider_air_conditioner.getValue() && slider_air_conditioner.getValue() < 100)
                    {
                        text_ac_status.setText("Windows up dont make a difference!");
                    }

                    else if (slider_air_conditioner.getValue() == 100)
                    {
                        text_ac_status.setText("Wind storm in my car!");
                    }
                }
            }
        });
        spinner_air_flow.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int air_flow = (Integer) spinner_air_flow.getValue();
                if(air_flow > 100)
                {
                    spinner_air_flow.setValue(100);
                }

                if(air_flow < 0)
                {
                    spinner_air_flow.setValue(0);
                }

                slider_air_conditioner.setValue(air_flow);
            }
        });


    }

    private boolean isTankEmpty()
    {
        boolean tankStatus;

        if(Double.parseDouble(text_gas_tank.getText()) == 0.0)
        {
            tankStatus = true;
            return tankStatus;
        }
        else
        {
            tankStatus = false;
            return tankStatus;
        }
    }

    private void decreaseTank()
    {
        double mpg = 25.00;
        double spentFuel = Double.parseDouble(text_gas_tank.getText()) - (slider_gas_pedal.getValue() / mpg);

        if(spentFuel > 0.00)
        {
            text_gas_tank.setText(df.format(spentFuel));
        }
        else if(spentFuel <= 0.00)
        {
            text_gas_tank.setText("0.0");
            button_stop_engine.setEnabled(false);
            slider_gas_pedal.setEnabled(false);
            button_start_engine.setEnabled(true);
            button_reset_odometer.setEnabled(true);
            button_refill_tank.setEnabled(true);
            forwardButton.setEnabled(false);
            reverseButton.setEnabled(false);
            rightButton.setEnabled(false);
            leftButton.setEnabled(false);
            button_brake_pedal.setEnabled(false);
            text_speedometer.setText("0");
            slider_gas_pedal.setValue(0);
        }
    }

    private void fillTank()
    {

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double tank_fill = Double.parseDouble(text_gas_tank.getText());

                if(filling_tank)
                {
                    if(tank_fill < 100 )
                    {
                        text_gas_tank.setText(String.valueOf((tank_fill) + 1));
                    }
                }
                if (tank_fill > 100)
                {
                    timer.stop();
                }
            }
        });

        timer.start();
        //text_gas_tank.setText("100");
    }

    private void stopFilling()
    {
        if(timer != null)
        {
            timer.stop();
        }
    }


    private void startCountDown()
    {
        String original = text_speedometer.getText();
        timer = new Timer(100, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int currentSpeed = Integer.parseInt(text_speedometer.getText());
                if(currentSpeed > 0)
                {
                    text_speedometer.setText(String.valueOf(currentSpeed - 1));
                    slider_gas_pedal.setValue(currentSpeed - 1);
                    spinner_gas_pedal.setValue(currentSpeed - 1);
                }

                if(currentSpeed <= 0)
                {
                    stopCountDown();
                }
            }
        });

        timer.start();
    }

    private void stopCountDown()
    {
        if(timer != null)
        {
            timer.stop();
        }
    }


    public void play_radio(String location)
    {
        File path = new File(location);
        music.setFile(path);
        music.play();
        music.loop();
    }




    public static void main(String[] args) throws InterruptedException {

        CarDiagram h = new CarDiagram();
        h.setContentPane(h.panel_main);
        h.setTitle("Car Simulator");
        h.setDefaultCloseOperation(EXIT_ON_CLOSE);
        h.setSize(860, 600);
        h.setLocationRelativeTo(null);
        h.setVisible(true);
    }
}
