package com.gambino_serra.condomanager_condomino.View.DrawerMenu.ListaFornitori;

import android.app.DialogFragment;

public class DialogChiamaFornitore extends DialogFragment {

//    String telefono;
//
//    public DialogChiamaFornitore() { }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        final Bundle bundle = getArguments();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        builder.setView(inflater.inflate(R.layout.old_dialog_chiama, null))
//
//                .setPositiveButton(R.string.chiama_fornitore_si, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        telefono = bundle.get("telefono").toString();
//                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telefono.trim()));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                })
//                .setNeutralButton(R.string.chiama_fornitore_no, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.M)
//                    public void onClick(DialogInterface dialog, int id) {
//                        dismiss();
//                    }
//                });
//        return builder.create();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        TextView testo = (TextView) this.getDialog().findViewById(R.id.textDescrizioneChiama);
//        testo.setText(R.string.chiama_fornitore);
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//    }
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
//    }

}

