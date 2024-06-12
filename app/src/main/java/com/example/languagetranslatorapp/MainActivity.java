package com.example.languagetranslatorapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    private TextToSpeech textToSpeech;
    private ImageButton b1;
    TextToSpeech tts;

    //String[] fromLanguages = {"English", "Arabic", "Armenian", "Bengali", "Chinese", "French", "German", "Gujarati", "Hindi", "Indonesian", "Italian", "Japanese", "Kannada", "Korean", "Marathi", "Persian", "Portuguese", "Spanish", "Tamil", "Telugu", "Thai", "Ukrainian", "Vietnamese"};


    //String[] toLanguages = {"To", "Arabic", "Armenian", "Bengali", "Chinese", "English", "French", "German", "Gujarati", "Hindi", "Indonesian", "Italian", "Japanese", "Kannada", "Korean", "Marathi", "Persian", "Portuguese", "Spanish", "Tamil", "Telugu", "Thai", "Ukrainian", "Vietnamese"};


    String[] fromLanguages = {"English", "Bengali", "Gujarati", "Hindi", "Kannada", "Marathi", "Tamil", "Telugu"};


    String[] toLanguages = {"To", "Bengali", "English", "Gujarati", "Hindi", "Kannada", "Marathi", "Tamil", "Telugu"};



    String lang;
    private static final int REQUEST_PERMISSION_CODE = 1;
    int languageCode, fromLanguageCode, toLanguageCode = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEdtSource);
        micIV = findViewById(R.id.idIVMic);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translatedTV = findViewById(R.id.idTVTranslatedTV);
        b1=(ImageButton)findViewById(R.id.imageButton);
        



        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);
                lang=toLanguages[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTV.setText("");
                if (Objects.requireNonNull(sourceEdt.getText()).toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your text to translate", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode == 0) {
                    Toast.makeText(MainActivity.this, "Please select source language", Toast.LENGTH_SHORT).show();
                } else if (toLanguageCode == 0) {
                    Toast.makeText(MainActivity.this, "Please select the language to make translation", Toast.LENGTH_SHORT).show();
                } else {
                    translateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i==TextToSpeech.SUCCESS){
                            switch(lang){
                                case "Arabic":
                                    tts.setLanguage(new Locale("ar"));
                                    break;
                                case "Armenian":
                                    // No specific accent for Armenian, using generic language code
                                    tts.setLanguage(new Locale("hy"));
                                    break;
                                case "Bengali":
                                    tts.setLanguage(new Locale("bn"));
                                    break;
                                case "Chinese":
                                    tts.setLanguage(Locale.CHINESE);
                                    break;
                                case "English":
                                    tts.setLanguage(Locale.ENGLISH);
                                    break;
                                case "French":
                                    tts.setLanguage(Locale.FRENCH);
                                    break;
                                case "German":
                                    tts.setLanguage(Locale.GERMAN);
                                    break;
                                case "Gujarati":
                                    tts.setLanguage(new Locale("gu"));
                                    break;
                                case "Hindi":
                                    tts.setLanguage(new Locale("hi"));
                                    break;
                                case "Indonesian":
                                    // No specific accent for Indonesian, using generic language code
                                    tts.setLanguage(new Locale("id"));
                                    break;
                                case "Italian":
                                    tts.setLanguage(Locale.ITALIAN);
                                    break;
                                case "Japanese":
                                    tts.setLanguage(Locale.JAPANESE);
                                    break;
                                case "Kannada":
                                    tts.setLanguage(new Locale("kn"));
                                    break;
                                case "Korean":
                                    tts.setLanguage(Locale.KOREAN);
                                    break;
                                case "Marathi":
                                    tts.setLanguage(new Locale("mr"));
                                    break;
                                case "Nepali":
                                    // No specific accent for Nepali, using generic language code
                                    tts.setLanguage(new Locale("ne"));
                                    break;
                                case "Persian":
                                    tts.setLanguage(new Locale("fa"));
                                    break;
                                case "Portuguese":
                                    tts.setLanguage(new Locale("pt"));
                                    break;
                                case "Punjabi":
                                    tts.setLanguage(new Locale("pa"));
                                    break;
                                case "Spanish":
                                    tts.setLanguage(new Locale("es"));
                                    break;
                                case "Tamil":
                                    tts.setLanguage(new Locale("ta"));
                                    break;
                                case "Telugu":
                                    tts.setLanguage(new Locale("te"));
                                    break;
                                case "Thai":
                                    tts.setLanguage(new Locale("th"));
                                    break;
                                case "Ukrainian":
                                    tts.setLanguage(new Locale("uk"));
                                    break;
                                case "Vietnamese":
                                    // No specific accent for Vietnamese, using generic language code
                                    tts.setLanguage(new Locale("vi"));
                                    break;
                                default:
                                    tts.setLanguage(Locale.ENGLISH);
                                    break;
                            }
                            tts.setSpeechRate(0.7f);
                            tts.speak(translatedTV.getText().toString(), TextToSpeech.QUEUE_ADD, null);
                        }
                    }
                });
            }
        });


        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into text");
                try {
                    startActivityForResult(i, REQUEST_PERMISSION_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert result != null;
                sourceEdt.setText(result.get(0));
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void translateText(int fromLanguageCode, int toLanguageCode, String source) {
        translatedTV.setText("Downloading Model.. ");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translatedTV.setText("Translating..");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedTV.setText(s);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Fail to translate :" + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Fail to download language model " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    public int getLanguageCode(String language) {
//        int languageCode = 0;
//        switch (language) {
//            case "English":
//                languageCode = FirebaseTranslateLanguage.EN;
//                break;
//            case "Gujarati":
//                languageCode = FirebaseTranslateLanguage.GU;
//                break;
//            case "Kannada":
//                languageCode = FirebaseTranslateLanguage.KN;
//                break;
//            case "Marathi":
//                languageCode = FirebaseTranslateLanguage.MR;
//                break;
//            case "Tamil":
//                languageCode = FirebaseTranslateLanguage.TA;
//                break;
//            case "Bengali":
//                languageCode = FirebaseTranslateLanguage.BN;
//                break;
//            case "Telugu":
//                languageCode = FirebaseTranslateLanguage.TE;
//                break;
//            case "Hindi":
//                languageCode = FirebaseTranslateLanguage.HI;
//                break;
//            case "Urdu":
//                languageCode = FirebaseTranslateLanguage.UR;
//                break;
//            default:
//                languageCode = 0;
//        }
//        return languageCode;
//    }



    public int getLanguageCode(String language) {
        int languageCode = 0;

        switch (language) {
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Chinese":
                languageCode = FirebaseTranslateLanguage.ZH;
                break;
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "French":
                languageCode = FirebaseTranslateLanguage.FR;
                break;
            case "German":
                languageCode = FirebaseTranslateLanguage.DE;
                break;
            case "Gujarati":
                languageCode = FirebaseTranslateLanguage.GU;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Indonesian":
                languageCode = FirebaseTranslateLanguage.ID;
                break;
            case "Italian":
                languageCode = FirebaseTranslateLanguage.IT;
                break;
            case "Japanese":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            case "Kannada":
                languageCode = FirebaseTranslateLanguage.KN;
                break;
            case "Korean":
                languageCode = FirebaseTranslateLanguage.KO;
                break;
            case "Marathi":
                languageCode = FirebaseTranslateLanguage.MR;
                break;
//            case "Nepali":
//                languageCode = FirebaseTranslateLanguage.NE;
//                break;
            case "Persian":
                languageCode = FirebaseTranslateLanguage.FA;
                break;
            case "Portuguese":
                languageCode = FirebaseTranslateLanguage.PT;
                break;
//            case "Punjabi":
//                languageCode = FirebaseTranslateLanguage.PL;
//                break;
            case "Spanish":
                languageCode = FirebaseTranslateLanguage.ES;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
                break;
            case "Telugu":
                languageCode = FirebaseTranslateLanguage.TE;
                break;
            case "Thai":
                languageCode = FirebaseTranslateLanguage.TH;
                break;
            case "Ukrainian":
                languageCode = FirebaseTranslateLanguage.UK;
                break;
            case "Vietnamese":
                languageCode = FirebaseTranslateLanguage.VI;
                break;
            default:
                languageCode = 0; // Invalid language or not supported by Firebase Translate
        }

        return languageCode;
    }

}
