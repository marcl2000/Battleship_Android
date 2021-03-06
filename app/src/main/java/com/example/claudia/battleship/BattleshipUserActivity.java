package com.example.claudia.battleship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.claudia.battleship.R.id.aiBoard;
import static com.example.claudia.battleship.R.id.userBoard;

public class BattleshipUserActivity extends AppCompatActivity {
    static int pos;
    static String gameStatus = "placingDestroyer";
    static boolean done = false;

    public static int getPosition(){
        return pos;
    }
    public static String getGameStatus(){
        return gameStatus;
    }

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.content_final_screen);
        //TextView textView = (TextView)findViewById(R.id.winlose);
        setContentView(R.layout.activity_battleship_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final GridView aiGridView = (GridView) findViewById(R.id.aiBoard);
        final GridView userGridView = (GridView) findViewById(R.id.userBoard);
        String[] tempAIArray = new String[100];
        for (int i = 0; i < 100; i++) {
            tempAIArray[i] = "~";
        }
        final List<String> AIArray = new ArrayList<String>(Arrays.asList(tempAIArray));

        String[] tempUserArray = new String[100];
        for (int i = 0; i < 100; i++) {
            tempUserArray[i] = "~";
        }
        String[] tempAIArrayCopy = new String[100];
        for (int i = 0; i < 100; i++) {
            tempUserArray[i] = "~";
        }

        final List<String> UserArray = new ArrayList<String>(Arrays.asList(tempUserArray));
        final List<String> AIArrayCopy = new ArrayList<String>(Arrays.asList(tempAIArrayCopy)); //for invisible placing of ships

        final ArrayAdapter<String> AIAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AIArray);
        aiGridView.setAdapter(AIAdapter);
        final ArrayAdapter<String> UserAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, UserArray);
        userGridView.setAdapter(UserAdapter);



        ((ArrayAdapter<String>)aiGridView.getAdapter()).notifyDataSetChanged();
        Board.placeAIShips(AIArrayCopy);

        //to test
        //Board.placeAIShips(AIArray);
        //((ArrayAdapter<String>)aiGridView.getAdapter()).notifyDataSetChanged();

        Toast.makeText(getApplication().getBaseContext(), "Welcome to Battleship!", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplication().getBaseContext(), "You will now choose your ships", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplication().getBaseContext(), "Choose the initial position of your Destroyer (size 2)", Toast.LENGTH_SHORT).show();

        aiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                pos = position;
                Board.turn(AIArray, AIArrayCopy);
                ((ArrayAdapter<String>) aiGridView.getAdapter()).notifyDataSetChanged();
                Board.AIturn(UserArray);
                ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();

                if (Board.scan(UserArray) == false) {
                    Intent i = new Intent(BattleshipUserActivity.this, FinalScreenActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplication().getBaseContext(), "YOU LOST!! TRY AGAIN", Toast.LENGTH_LONG).show();
                    done = true;

                }else if (Board.scan(AIArrayCopy) == false) {
                    Intent i = new Intent(BattleshipUserActivity.this, FinalScreenActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplication().getBaseContext(), "YOU WON!! CONGRATULATIONS", Toast.LENGTH_LONG).show();
                    done = true;


                } else if (done == false) {
                    Toast.makeText(getApplication().getBaseContext(), "Choose your next point of attack.", Toast.LENGTH_SHORT).show();
                    done=true;
                }
            }
        });



        userGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                pos=position;

                if(gameStatus=="placingDestroyer") {

                    Board.placeStart(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "placingDestroyerFinish";
                    Toast.makeText(getApplication().getBaseContext(), "Tap the coordinate next to the initial in the direction of your ship", Toast.LENGTH_SHORT).show();
                }
                else if (gameStatus == "placingDestroyerFinish"){

                    Board.placeFinish(UserArray);
                    ((ArrayAdapter<String>)userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus="placingSubmarine";
                    Toast.makeText(getApplication().getBaseContext(), "Choose the initial position of your Submarine (size 3)", Toast.LENGTH_SHORT).show();
                    }

                else if(gameStatus=="placingSubmarine"){

                    Board.placeStart(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "placingSubmarineFinish";
                    //Toast.makeText(getApplication().getBaseContext(), "Tap the coordinate next to the initial in the direction of your ship", Toast.LENGTH_LONG).show();
                }
                else if(gameStatus=="placingSubmarineFinish"){

                    Board.placeFinish(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "placingCruiser";
                    Toast.makeText(getApplication().getBaseContext(), "Choose the initial position of your Cruiser (size 3)", Toast.LENGTH_SHORT).show();
                }
                else if(gameStatus=="placingCruiser"){

                    Board.placeStart(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "placingCruiserFinish";
                    //Toast.makeText(getApplication().getBaseContext(), "Tap the coordinate next to the initial in the direction of your ship", Toast.LENGTH_LONG).show();
                }
                else if(gameStatus=="placingCruiserFinish"){

                    Board.placeFinish(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "placingBattleship";
                    Toast.makeText(getApplication().getBaseContext(), "Choose the initial position of your Battleship (size 4)", Toast.LENGTH_SHORT).show();
                }
                else if(gameStatus=="placingBattleship"){

                    Board.placeStart(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "placingBattleshipFinish";
                    //Toast.makeText(getApplication().getBaseContext(), "Tap the coordinate next to the initial in the direction of your ship", Toast.LENGTH_LONG).show();
                }
                else if(gameStatus=="placingBattleshipFinish"){

                    Board.placeFinish(UserArray);
                    ((ArrayAdapter<String>) userGridView.getAdapter()).notifyDataSetChanged();
                    gameStatus = "Ready";
                    Toast.makeText(getApplication().getBaseContext(), "You are ready to play! Choose your point of attack.", Toast.LENGTH_SHORT).show();
                }
            }
        });




        //









        /*int board[][] = new int[10][10];
        initBoard(board, false);
        int AIboard[][] = new int[10][10];
        initBoard(AIboard, true);

        System.out.println("You will now place your ships.");
        generateShip(board);
        System.out.println("");
        System.out.println("**********************************");
        displayBoard(board, false);

        runGame(board, AIboard);
    }
    */


    }
}

