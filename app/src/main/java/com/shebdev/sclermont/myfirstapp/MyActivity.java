package com.shebdev.sclermont.myfirstapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shebdev.sclermont.myfirstapp.adapter.MessagePartRecyclerAdapter;
import com.shebdev.sclermont.myfirstapp.db.MessageAssemblyData;
import com.shebdev.sclermont.myfirstapp.db.MessageAssemblyLinkData;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;
import com.shebdev.sclermont.myfirstapp.dialog.ErrorDialogFragment;

import java.util.ArrayList;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_GENERATED_MESSAGE = "com.shebdev.sclermont.myfirstapp.MESSAGE";
    public final static String EXTRA_AUDIO_FILE_NAME_LIST = "com.shebdev.sclermont.myfirstapp.EXTRA_AUDIO_FILE_NAME_LIST";
    public final static String EXTRA_MESSAGE_PART = "com.shebdev.sclermont.myfirstapp.MESSAGE_PART";
    public final static String EXTRA_MESSAGE_PART_AUDIO_FILE = "com.shebdev.sclermont.myfirstapp.MESSAGE_PART_AUDIO_FILE";
    public final static String EXTRA_MESSAGE_PART_POSITION = "com.shebdev.sclermont.myfirstapp.MESSAGE_PART_POSITION";
    public final static String EXTRA_MESSAGE_PART_ASSEMBLY_ID = "com.shebdev.sclermont.myfirstapp.MESSAGE_PART_ASSEMBLY_ID";
    public final static String EXTRA_MESSAGE_PART_ORDER = "com.shebdev.sclermont.myfirstapp.MESSAGE_PART_ORDER";
    public final static String EXTRA_ADD_DATE = "com.shebdev.sclermont.myfirstapp.ADD_DATE";
    public final static String EXTRA_LOAD_GREETING = "com.shebdev.sclermont.myfirstapp.LOAD_GREETING";
    public final static String ERROR_MESSAGE = "com.shebdev.sclermont.myfirstapp.ERROR_MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

    private static final int REQUEST_CODE_ASSEMBLY_LIST = 444;
    private static final int REQUEST_CODE_MESSAGE_PART_ADD = 555;
    private static final int REQUEST_CODE_MESSAGE_PART_SELECT = 666;
    private static final int REQUEST_CODE_GREETING_SELECT = 777;
    public static final int REQUEST_CODE_MESSAGE_PART_EDIT = 888;
    private RecyclerView mRecyclerView;
    private MessagePartRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String greetingAudioFileName; // TODO: Voir à gerer via les preferences quand on va sauvegarder les autres preferences comme le dernier assemblyId chargé

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.phone);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_assembly_title);

        editor.putString("editNom", editNom.getText().toString());
        editor.putString("editFinMsg", editFinMsg.getText().toString());

        // Commit the edits!
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_assembly_title);

        editNom.setText(settings.getString("editNom", ""));
        //editFinMsg.setText(settings.getString("editFinMsg", ""));
        long assemblyId = settings.getLong("assemblyId", -1l);

        //Toast.makeText(this, "AssId" + assemblyId, Toast.LENGTH_LONG).show();

        if (mAdapter == null) {

            //Toast.makeText(this, "adapter null", Toast.LENGTH_LONG).show();
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (assemblyId < 0) {
                ArrayList<MessagePartData> dset = new ArrayList<>();
                mAdapter = new MessagePartRecyclerAdapter(dset, -1l);
            }
            else {
                //Toast.makeText(this, "load existing assembly", Toast.LENGTH_LONG).show();
                ArrayList<MessagePartData> dset = loadAssemblyLinkData(assemblyId);
                mAdapter = new MessagePartRecyclerAdapter(dset, assemblyId);

                MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
                ArrayList<MessageAssemblyLinkData> dataSet = dbHelper.getAssemblyLinkData(assemblyId, true);
                if (dataSet.size() > 0) {
                    MessagePartData mpd = dbHelper.getMessagePart(Long.valueOf(dataSet.get(0).getPartId()));
                    ((EditText) findViewById(R.id.edit_greeting)).setText(mpd.getText());
                }
            }

            mRecyclerView.setAdapter(mAdapter);
        }

    }

    /** Called when the user clicks the Send button */
    public void genererMessage(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editGreeting = (EditText) findViewById(R.id.edit_greeting);
        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        CheckBox addDateToMessage = (CheckBox) findViewById(R.id.checkbox_add_date);

        if (editGreeting == null || editGreeting.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.empty_greeting));
            showErrorDialog(bundle);
            return;
        }

        if (editNom == null || editNom.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.empty_name));
            showErrorDialog(bundle);
            return;
        }

        ArrayList<MessagePartData> dataSet = mAdapter.getMDataset();
        if (dataSet.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.empty_assembly));
            showErrorDialog(bundle);
            return;
        }

        StringBuilder generatedMessage = new StringBuilder();

        ArrayList<String> message = new ArrayList<>();
        ArrayList<String> audioFileNameList = new ArrayList<>();
        message.add(editGreeting.getText().toString());
        message.add(editNom.getText().toString());
        for (MessagePartData mpd : dataSet) {
            generatedMessage.append(mpd.getText());
            generatedMessage.append(" ");
            audioFileNameList.add(mpd.getAudioFileName());
        }
        message.add(generatedMessage.toString());
        intent.putExtra(EXTRA_GENERATED_MESSAGE, message);
        intent.putExtra(EXTRA_AUDIO_FILE_NAME_LIST, audioFileNameList);
        intent.putExtra(EXTRA_ADD_DATE, addDateToMessage.isChecked());
        startActivity(intent);

    }

    public void chargerAssembly(View view) {
        Intent intent = new Intent(this, MessageAssemblyListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ASSEMBLY_LIST);
    }

    public void ajouterLigne(View view) {
        Intent intent = new Intent(this, MessagePartEdit.class);
        startActivityForResult(intent, REQUEST_CODE_MESSAGE_PART_ADD);
    }

    public void ajouterLigneExistante(View view) {
        Intent intent = new Intent(this, MessagePartSelectActivity.class);
        startActivityForResult(intent, REQUEST_CODE_MESSAGE_PART_SELECT);
    }

    public void loadExistingGreeting(View view) {
        Intent intent = new Intent(this, MessagePartSelectActivity.class);
        intent.putExtra(EXTRA_LOAD_GREETING, true);
        startActivityForResult(intent, REQUEST_CODE_GREETING_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String messagePart;
        MessagePartData mpd;

        // Check which request we're responding to
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_MESSAGE_PART_SELECT:
                    mpd = new MessagePartData(data.getStringExtra("message"), null);
                    mAdapter.addLine(mpd);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
                    break;
                case REQUEST_CODE_GREETING_SELECT:
                    messagePart = data.getStringExtra("message");
                    ((EditText) findViewById(R.id.edit_greeting)).setText(messagePart);
                    break;
                case REQUEST_CODE_MESSAGE_PART_ADD:
                    mpd = new MessagePartData(data.getStringExtra("message"),
                            data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE));
                    // TODO: Récupérer nom fichier audio et l'ajouter a l'enregistrement si nécessaire seulement
                    // TODO: Je n'ai plus le choix, le dataset de l'adapter doit etre une liste de "MessagePartData" pour pouvoir avoir le nom de fichier audio car si on reordonne ca fout le bordel vu que les fichiers audio sont nommes avec l'ass_id+l'order+un timestamp
                    mAdapter.addLine(mpd);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
                    break;
                case REQUEST_CODE_MESSAGE_PART_EDIT:
                    mpd = new MessagePartData(data.getStringExtra("message"),
                            data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE));
                    // TODO: Récupérer nom fichier audio et le mettre à jour si nécessaire seulement
                    int position = data.getIntExtra(EXTRA_MESSAGE_PART_POSITION, 0);
                    mAdapter.editLine(mpd, position);
                    break;
                case REQUEST_CODE_ASSEMBLY_LIST:
                    long assemblyId = data.getLongExtra("assemblyId", -1l);
                    if (assemblyId >= 0) {
                        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
                        MessageAssemblyData mad = dbHelper.getAssemblyData(assemblyId);
                        EditText editAssemblyTitle = (EditText) findViewById(R.id.edit_assembly_title);
                        EditText editAssemblyDescription = (EditText) findViewById(R.id.edit_assembly_description);
                        editAssemblyTitle.setText(mad.getTitle());
                        editAssemblyDescription.setText(mad.getDescription());
                        mAdapter.changeMDataSet(loadAssemblyLinkData(assemblyId));

                        // TODO: Voir a integrer mieux le load des assembly en rapoort a l'accueil
                        // Voir ici et dans onResume
                        ArrayList<MessageAssemblyLinkData> dataSet = dbHelper.getAssemblyLinkData(assemblyId, true);
                        if (dataSet.size() > 0) {
                            mpd = dbHelper.getMessagePart(Long.valueOf(dataSet.get(0).getPartId()));
                            ((EditText) findViewById(R.id.edit_greeting)).setText(mpd.getText());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<MessagePartData> loadAssemblyLinkData(long assemblyId) {
        ArrayList<MessagePartData> dset = new ArrayList<>();
        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        ArrayList<MessageAssemblyLinkData> dataSet = dbHelper.getAssemblyLinkData(assemblyId, false);

        for (MessageAssemblyLinkData mald : dataSet) {
            //Toast.makeText(this, "mpd::"+mald.getId(), Toast.LENGTH_LONG).show();
            MessagePartData mpd = dbHelper.getMessagePart(Long.parseLong(mald.getPartId()));

            dset.add(mpd);
        }

        return dset;
    }

    private void showErrorDialog(Bundle bundle) {
        DialogFragment newFragment = new ErrorDialogFragment();
        newFragment.show(getFragmentManager(), "error_message");
        newFragment.setArguments(bundle);
    }

    public void sauvegarderMessage(View view) {

        String editGreeting = ((EditText) findViewById(R.id.edit_greeting)).getText().toString();
        String editAssemblyTitle = ((EditText) findViewById(R.id.edit_assembly_title)).getText().toString();
        String editAssemblyDescription = ((EditText) findViewById(R.id.edit_assembly_description)).getText().toString();

        if (editGreeting == null || editGreeting.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.empty_greeting));
            showErrorDialog(bundle);
            return;
        }

        if (editAssemblyTitle == null || editAssemblyTitle.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.empty_assembly_title));
            showErrorDialog(bundle);
            return;
        }

        ArrayList<MessagePartData> dataSet = mAdapter.getMDataset();

        if (dataSet.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.empty_assembly));
            showErrorDialog(bundle);
            return;
        }

        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        long mPartId;
        long assemblyId;
        long linkId;

        MessageAssemblyData mad = dbHelper.getAssemblyDataWhereTitleIs(editAssemblyTitle);
        if (mad == null) {
            assemblyId = dbHelper.createPartAssembly(editAssemblyTitle, editAssemblyDescription);
        }
        else {
            assemblyId = mad.get_id();
            if (!mad.getDescription().equals(editAssemblyDescription)) {
                dbHelper.updatePartAssembly(assemblyId, editAssemblyDescription);
            }
        }
        long po = 1l;


        MessagePartData mpd = dbHelper.getMessagePartWhereTextIs(editGreeting, true);
        if (mpd == null) {
            mPartId = dbHelper.createMessagePart(editGreeting, true, "");
        }
        else {
            mPartId = mpd.get_id();
            //dbHelper.updateMessagePartAudioFileName()
        }

        // TODO: Voir si on met ça dans une méthode du helper de BD ou dans une méthode private ici
        MessageAssemblyLinkData mald = dbHelper.getAssemblyLinkDataWhereAssemblyIdPartId(String.valueOf(assemblyId),
                String.valueOf(mPartId));
        if (mald == null) {
            // Check if there was already a greeting for that assembly
            ArrayList<MessageAssemblyLinkData> assemblyGreeting = dbHelper.getAssemblyLinkData(assemblyId, true);
            if (assemblyGreeting.size() > 0) {
                // TODO: Mettre un indicateur dans le assembly link pour identifier les liens pour l'accueil à la place de mettre 0
                dbHelper.deletePartAssemblyLink(((MessageAssemblyLinkData) assemblyGreeting.get(0)).getId());
            }
            linkId = dbHelper.createPartAssemblyLink(assemblyId, mPartId, 0l);
        }

        // TODO: Utiliser les MessagePartData directement dans la liste pour mieux gérer la persistance en bd
        ArrayList<Long> listIdLink = new ArrayList<>();
        for (MessagePartData mpdFromDataSet : dataSet) {
            MessagePartData messagePartData = dbHelper.getMessagePartWhereTextIs(mpdFromDataSet.toString(), false);
            if (messagePartData == null) {
                mPartId = dbHelper.createMessagePart(mpdFromDataSet.toString(), false, mpdFromDataSet.getAudioFileName());
            }
            else {
                mPartId = messagePartData.get_id();
                dbHelper.updateMessagePartAudioFileName(mPartId, mpdFromDataSet.getAudioFileName());
            }
            mald = dbHelper.getAssemblyLinkDataWhereAssemblyIdPartId(String.valueOf(assemblyId),
                    String.valueOf(mPartId));
            if (mald == null) {
                linkId = dbHelper.createPartAssemblyLink(assemblyId, mPartId, po);
            }
            else {
                linkId = mald.getId();
                if (po != Long.parseLong(mald.getPartOrder())) {
                    dbHelper.updatePartAssemblyLink(mald.getId(), po);
                }
            }
            listIdLink.add(linkId);
            po++;
        }

        // Faire un cleanup de link inutiles
        ArrayList<MessageAssemblyLinkData> almald = dbHelper.getAssemblyLinkData(assemblyId, false);
        for(MessageAssemblyLinkData maldCleanup : almald) {
            if (!listIdLink.contains(maldCleanup.getId())) {
                dbHelper.deletePartAssemblyLink(maldCleanup.getId());
            }
        }

    }

    public void savePrefs(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        //EditText editAccueil = (EditText) findViewById(R.id.edit_accueil);
//        EditText editNom = (EditText) findViewById(R.id.edit_nom);
//        EditText editFinMsg = (EditText) findViewById(R.id.edit_assembly_title);
//        ArrayList<String> message = new ArrayList<String>();
//        //message.add(editAccueil.getText().toString());
//        message.add(editNom.getText().toString());
//        message.add(editFinMsg.getText().toString());
    }

    public void openSearch() {
        System.out.println("Open search");
    }

    public void openSettings() {
        System.out.println("Open settings");
    }

}
