package com.shebdev.sclermont.myfirstapp;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
    public final static String EXTRA_LOAD_GREETING = "com.shebdev.sclermont.myfirstapp.EXTRA_LOAD_GREETING";
    public final static String EXTRA_ADD_DATE = "com.shebdev.sclermont.myfirstapp.ADD_DATE";
    public final static String ERROR_MESSAGE = "com.shebdev.sclermont.myfirstapp.ERROR_MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

    private static final int REQUEST_CODE_ASSEMBLY_LIST = 444;
    private static final int REQUEST_CODE_MESSAGE_PART_ADD = 555;
    private static final int REQUEST_CODE_MESSAGE_PART_SELECT = 666;
    private static final int REQUEST_CODE_GREETING_ADD_EDIT_SELECT = 777;
    public static final int REQUEST_CODE_MESSAGE_PART_EDIT = 888;
    private RecyclerView mRecyclerView;
    private MessagePartRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String greetingAudioFileName; // TODO: Voir à gerer via les preferences quand on va sauvegarder les autres preferences comme le dernier assemblyId chargé

    private int tutorialStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.phone);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        // TODO: Désactivation temporaire de l'aide
//        findViewById(R.id.layout_tutorial).setVisibility(View.INVISIBLE);
//        ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_welcome);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                openSearch();
//                return true;
//            case R.id.action_settings:
//                openSettings();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onPause(){
        super.onPause();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_assembly_title);
        CheckBox chkAddDate = (CheckBox) findViewById(R.id.checkbox_add_date);

        editor.putString("editNom", editNom.getText().toString());
        editor.putString("editFinMsg", editFinMsg.getText().toString());
        editor.putBoolean("chkAddDate", chkAddDate.isChecked());

        // Commit the edits!
        editor.commit();
    }

    private void saveAssemblyId(long assemblyId) {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putLong("assemblyId", assemblyId);

        // Commit the edits!
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ((EditText) findViewById(R.id.edit_nom)).setText(settings.getString("editNom", ""));
        ((CheckBox) findViewById(R.id.checkbox_add_date)).setChecked(settings.getBoolean("chkAddDate", false));

        long assemblyId = settings.getLong("assemblyId", -1l);

        if (mAdapter == null) {

            //Toast.makeText(this, "adapter null", Toast.LENGTH_LONG).show();
            mRecyclerView = (RecyclerView) findViewById(R.id.message_assembly_recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // Pour désactiver le scroll de la page principale quand on scroll dans le recycler view
            mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    int action = e.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_SCROLL:
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_UP:
                            rv.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                }

            });

            if (assemblyId < 0) {
                ArrayList<MessagePartData> dset = new ArrayList<>();
                mAdapter = new MessagePartRecyclerAdapter(dset, -1l);

            }
            else {
                //Toast.makeText(this, "load existing assembly", Toast.LENGTH_LONG).show();
                ArrayList<MessagePartData> dset = loadAssemblyLinkData(assemblyId);
                mAdapter = new MessagePartRecyclerAdapter(dset, assemblyId);

                MessagePartData mpd;

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
                    ((TextView) findViewById(R.id.text_greeting)).setText(mpd.getText());
                    greetingAudioFileName = mpd.getAudioFileName();
                }

                adapterChanged();

            }

            mRecyclerView.setAdapter(mAdapter);
        }

    }

    /** Called when the user clicks the Send button */
    public void genererMessage(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        TextView editGreeting = (TextView) findViewById(R.id.text_greeting);
        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        CheckBox addDateToMessage = (CheckBox) findViewById(R.id.checkbox_add_date);

        if (editGreeting == null || editGreeting.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.error_empty_greeting));
            showErrorDialog(bundle);
            return;
        }

        if (editNom == null || editNom.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.error_empty_name));
            showErrorDialog(bundle);
            return;
        }

        ArrayList<MessagePartData> dataSet = mAdapter.getMDataset();
        if (dataSet.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.error_empty_assembly));
            showErrorDialog(bundle);
            return;
        }

        StringBuilder generatedMessage = new StringBuilder();
        ArrayList<String> audioFileNameList = new ArrayList<>();

        if (greetingAudioFileName != null && greetingAudioFileName.length() > 0) {
            audioFileNameList.add(greetingAudioFileName);
        }

        ArrayList<String> message = new ArrayList<>();
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

    public void editGreeting(View view) {
        Intent intent = new Intent(this, MessagePartEdit.class);
        intent.putExtra(MyActivity.EXTRA_MESSAGE_PART, ((TextView)findViewById(R.id.text_greeting)).getText());
        intent.putExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE, greetingAudioFileName);
        startActivityForResult(intent, REQUEST_CODE_GREETING_ADD_EDIT_SELECT);
    }

    public void addGreeting(View view) {
        Intent intent = new Intent(this, MessagePartEdit.class);
        startActivityForResult(intent, REQUEST_CODE_GREETING_ADD_EDIT_SELECT);
    }

    public void loadExistingGreeting(View view) {
        Intent intent = new Intent(this, MessagePartSelectActivity.class);
        intent.putExtra(EXTRA_LOAD_GREETING, true);
        startActivityForResult(intent, REQUEST_CODE_GREETING_ADD_EDIT_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String messagePart;
        MessagePartData mpd;

        // Check which request we're responding to
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_MESSAGE_PART_SELECT:
                    mpd = new MessagePartData(data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART), null);
                    mAdapter.addLine(mpd);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
                    break;
                case REQUEST_CODE_GREETING_ADD_EDIT_SELECT:
                    messagePart = data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART);
                    ((TextView) findViewById(R.id.text_greeting)).setText(messagePart);
                    greetingAudioFileName = data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE);
                    break;
                case REQUEST_CODE_MESSAGE_PART_ADD:
                    mpd = new MessagePartData(data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART),
                            data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE));
                    mAdapter.addLine(mpd);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
                    break;
                case REQUEST_CODE_MESSAGE_PART_EDIT:
                    mpd = new MessagePartData(data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART),
                            data.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE));
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
                            ((TextView) findViewById(R.id.text_greeting)).setText(mpd.getText());
                            greetingAudioFileName = mpd.getAudioFileName();
                        }

                        saveAssemblyId(assemblyId);
                    }
                    break;
                default:
                    break;
            }
            adapterChanged();
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

    public void tutorialNextStep(View view) {
//        switch (tutorialStep) {
//            case 1:
//                findViewById(R.id.pointer_message_title).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_message_title);
//                break;
//            case 2:
//                findViewById(R.id.pointer_message_title).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_message_description).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_message_description);
//                break;
//            case 3:
//                findViewById(R.id.pointer_message_description).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_greeting).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_message_greeting);
//                break;
//            case 4:
//                findViewById(R.id.pointer_greeting).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_new_greeting).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_add_new_greeting);
//                break;
//            case 5:
//                findViewById(R.id.pointer_new_greeting).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_existing_greeting).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_add_existing_greeting);
//                break;
//            case 6:
//                findViewById(R.id.pointer_existing_greeting).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_name).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_name);
//                break;
//            case 7:
//                findViewById(R.id.pointer_name).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_add_date).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_date);
//                break;
//            case 8:
//                findViewById(R.id.pointer_add_date).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_static_recordings).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_static_voice_recordings);
//                break;
//            case 9:
//                findViewById(R.id.pointer_static_recordings).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_message_part_list).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_message_part_list);
//                break;
//            case 10:
//                findViewById(R.id.pointer_message_part_list).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_new_message_part).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_add_new_message_part);
//                break;
//            case 11:
//                findViewById(R.id.pointer_new_message_part).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_existing_message_part).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_add_existing_message_part);
//                break;
//            case 12:
//                findViewById(R.id.pointer_existing_message_part).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_generate_message).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_generate_message);
//                break;
//            case 13:
//                findViewById(R.id.pointer_generate_message).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_save).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_save_message);
//                break;
//            case 14:
//                findViewById(R.id.pointer_save).setVisibility(View.INVISIBLE);
//                findViewById(R.id.pointer_load).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.text_turorial)).setText(R.string.tutorial_load_message);
//                break;
//            default:
//                findViewById(R.id.layout_tutorial).setVisibility(View.INVISIBLE);
//        }
//        tutorialStep++;
    }

    public void sauvegarderMessage(View view) {

        String editGreeting = ((TextView) findViewById(R.id.text_greeting)).getText().toString();
        String editAssemblyTitle = ((EditText) findViewById(R.id.edit_assembly_title)).getText().toString();
        String editAssemblyDescription = ((EditText) findViewById(R.id.edit_assembly_description)).getText().toString();

        if (editGreeting == null || editGreeting.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.error_empty_greeting));
            showErrorDialog(bundle);
            return;
        }

        if (editAssemblyTitle == null || editAssemblyTitle.length() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.error_empty_assembly_title));
            showErrorDialog(bundle);
            return;
        }

        ArrayList<MessagePartData> dataSet = mAdapter.getMDataset();

        if (dataSet.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_MESSAGE, getString(R.string.error_empty_assembly));
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
        saveAssemblyId(assemblyId);
        long po = 1l;


        MessagePartData mpd = dbHelper.getMessagePartWhereTextIs(editGreeting, true);
        if (mpd == null) {
            mPartId = dbHelper.createMessagePart(editGreeting, true, greetingAudioFileName);
        }
        else {
            mPartId = mpd.get_id();
            // TODO: Gerer audio pour les greetings aussi
            dbHelper.updateMessagePartAudioFileName(mPartId, greetingAudioFileName);
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
            MessagePartData messagePartData = dbHelper.getMessagePartWhereTextIs(mpdFromDataSet.getText(), false);
            if (messagePartData == null) {
                mPartId = dbHelper.createMessagePart(mpdFromDataSet.getText(), false, mpdFromDataSet.getAudioFileName());
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
//        //EditText editAccueil = (EditText) findViewById(R.id.hint_edit_greeting);
//        EditText editNom = (EditText) findViewById(R.id.hint_edit_name);
//        EditText editFinMsg = (EditText) findViewById(R.id.edit_assembly_title);
//        ArrayList<String> message = new ArrayList<String>();
//        //message.add(editAccueil.getText().toString());
//        message.add(editNom.getText().toString());
//        message.add(editFinMsg.getText().toString());
    }

    public void setStaticAudioRecordings(View view) {
        Intent intent = new Intent(this, StaticAudioRecordingsActivity.class);
        startActivity(intent);
    }

    public void openSearch() {
        System.out.println("Open search");
    }

    public void openSettings() {
        System.out.println("Open settings");
    }



    public MessagePartRecyclerAdapter getMAdapter() {
        return mAdapter;
    }

    public void adapterChanged() {
        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            findViewById(R.id.text_message_part_instructions).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.text_message_part_instructions).setVisibility(View.VISIBLE);
        }
    }
}
