package com.profitz.app.promos.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragmentTransparent;
import com.profitz.app.R;import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.data.DoneSender;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BottomSheetDialogInsertDone extends SuperBottomSheetFragmentTransparent {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    String date_choosen;
    String promo_email;
    View view;
    int user_lvl;
    double promo_earn_before;
    double promo_earn_bonus;
    double promo_before_calculate;
    EditText editTextUsernameAccountNumber;
    EditText editTextAdditionalInfo;
    int user_id;
    String user_nickname;
    String user_email;
    int promo_id;
    String promo_name;
    String promo_type2;
    String promo_number_phone;
    String optional;
    TextView dialog_done_info2;
    TextView dialog_done_info3;
    String promo_earn;
    RelativeLayout rl_done_global;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        promo_name = mArgs.getString("promo_name");
        if (promo_name.equals("bitFlyer")){
            view = inflater.inflate(R.layout.dialog_done_bitflyer, container, false);

        }
        else{
            view = inflater.inflate(R.layout.dialog_done, container, false);
        }
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            rl_done_global = view.findViewById(R.id.rl_done_global);
            //this.getDialog().getWindow().setBackgroundDrawableResource(R.drawable.charge_layout_background);
            //rl_done_global.setBackgroundColor(getResources().getColor(R.color.red));
           // rl_done_global.setAlpha(0.0f);
            dialog_done_info2 = view.findViewById(R.id.dialog_done_info2);
              dialog_done_info3 = view.findViewById(R.id.dialog_done_info3);
        ((PromoDetailActivity)getActivity()).stopSlider();




            user_id = mArgs.getInt("user_id");
            user_nickname = mArgs.getString("user_nickname");
            user_email = mArgs.getString("user_email");
            promo_id = mArgs.getInt("promo_id");
            promo_type2 = mArgs.getString("promo_type2");
            promo_earn = mArgs.getString("promo_earn");
            user_lvl = mArgs.getInt("user_lvl");
            promo_earn_before= Float.parseFloat(promo_earn);
            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            //promoId = PromoDetailActivity.getId();

          //  new AsyncLogin().execute();
            //  dismiss(); - zamyka modal
            //view.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            final SingleDateAndTimePicker singleDateAndTimePicker = view.findViewById( R.id.single_day_picker );
            // Example for setting default selected date to yesterday
    //        Calendar instance = Calendar.getInstance();
    //        instance.add(Calendar.DATE, -1 );
    //        singleDateAndTimePicker.setDefaultDate(instance.getTime());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 0);
            Date maxDate = cal.getTime();

            singleDateAndTimePicker.selectDate(cal);
            singleDateAndTimePicker.setMaxDate(maxDate);

            DateFormat formatter = new SimpleDateFormat("EEE d MMM HH:mm:ss");
            date_choosen = formatter.format(singleDateAndTimePicker.getDate());


            SingleDateAndTimePicker.OnDateChangedListener changeListener = (displayed, date) -> {
                display(displayed);
            };
            singleDateAndTimePicker.addOnDateChangedListener( changeListener );

            TextView click = (TextView) view.findViewById(R.id.button_done_confirm);
            final EditText shelf_name_edit=(EditText) view.findViewById(R.id.editTextUsernameDone);
            String hint="Podaj email podany podczas rejestracji w " + promo_name;
            shelf_name_edit.setHint(hint);
            String dialog_done_info3_text = "Wprowadź datę rejestracji w "+promo_name;
             dialog_done_info3.setText(dialog_done_info3_text );
        if (user_lvl == 0){
            promo_before_calculate = promo_earn_before*0.01;
            promo_earn_bonus = promo_before_calculate/4.35;
        }
        else if (user_lvl ==1){
            promo_before_calculate = promo_earn_before*0.10;
            promo_earn_bonus = promo_before_calculate/4.35;
        }
        else if (user_lvl == 2){
            promo_before_calculate = promo_earn_before*0.20;
            promo_earn_bonus = promo_before_calculate/4.35;
        }
        else if (user_lvl == 3){
            promo_before_calculate = promo_earn_before*0.30;
            promo_earn_bonus = promo_before_calculate/4.35;
        }
        else if (user_lvl == 4){
            promo_before_calculate = promo_earn_before*0.40;
            promo_earn_bonus = promo_before_calculate/4.35;
        }
        else if (user_lvl == 5){
            promo_before_calculate = promo_earn_before*0.50;
            promo_earn_bonus = promo_before_calculate/4.35;
        }
        DecimalFormat precision = new DecimalFormat("0.00");
        double promo_earn_bonus_decimal = promo_earn_bonus;
        String calculated = precision.format(promo_earn_bonus_decimal);
        if (promo_name.equals("bitFlyer")){
            String text_with_points =  "By otrzymać nagrodę w postaci " + calculated+" punktów, oraz 5,00€ za wykonanie promocji uzupełnij poniższe pola. Po pozytywnym zweryfikowaniu przez administratorów, punty zostaną przydzielone do Twojego konta.\n Wypłaty bonusów w bitFlyer następują każdego 10-go dnia miesiąca (tylko dni robocze).";
            dialog_done_info2.setText(text_with_points );
        }
        else{
            String text_with_points =  "By otrzymać nagrodę w postaci " + calculated+" punktów za wykonanie promocji uzupełnij poniższe pola. Po pozytywnym zweryfikowaniu przez administratorów, punty zostaną przydzielone do Twojego konta.";
            dialog_done_info2.setText(text_with_points );
        }
        if (promo_name.equals("bitFlyer")){


            editTextUsernameAccountNumber = view.findViewById(R.id.editTextUsernameAccountNumber);
            editTextAdditionalInfo = view.findViewById(R.id.editTextAdditionalInfo);

        editTextUsernameAccountNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                final String edit12 = shelf_name_edit.getText().toString();

                if (TextUtils.isEmpty(edit12)) {
                    editTextUsernameAccountNumber.setError("Wprowadź numer telefonu blik");
                    editTextUsernameAccountNumber.requestFocus();
                   // click.setEnabled(false);
                } else {
                    click.setEnabled(true);
                    editTextUsernameAccountNumber.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String edit12 = editTextUsernameAccountNumber.getText().toString();
                String regexStr = "^[0-9]{9,13}$";

                if (TextUtils.isEmpty(edit12)) {
                    editTextUsernameAccountNumber.setError("Wprowadź numer telefonu blik");
                    editTextUsernameAccountNumber.requestFocus();
                    //click.setEnabled(false);
                } else if (editTextUsernameAccountNumber.toString().length()<9 || edit12.length()>13 || !edit12.matches(regexStr)) {
                    editTextUsernameAccountNumber.setError("Wprowadź poprawny numer telefonu.");
                    editTextUsernameAccountNumber.requestFocus();
                   // click.setEnabled(false);
                } else {
                    click.setEnabled(true);
                    editTextUsernameAccountNumber.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String edit12 = editTextUsernameAccountNumber.getText().toString();
                String regexStr = "^[0-9]{9,13}$";

                if (TextUtils.isEmpty(edit12)) {
                    editTextUsernameAccountNumber.setError("Wprowadź numer telefonu blik");
                    editTextUsernameAccountNumber.requestFocus();
                    //click.setEnabled(false);
                } else if (editTextUsernameAccountNumber.toString().length()<9 || edit12.length()>13 || !edit12.matches(regexStr)) {
                    editTextUsernameAccountNumber.setError("Wprowadź poprawny numer telefonu.");
                    editTextUsernameAccountNumber.requestFocus();
                   // click.setEnabled(false);
                } else {
                    click.setEnabled(true);
                    editTextUsernameAccountNumber.setError(null);
                }
            }
        });
        }
            //cada vez que se modifique texto llamar
            shelf_name_edit.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    final String edit11 = shelf_name_edit.getText().toString();

                    if (TextUtils.isEmpty(edit11)) {
                        shelf_name_edit.setError("Wprowadź adres email.");
                        shelf_name_edit.requestFocus();
                        click.setEnabled(false);
                    } else {
                        shelf_name_edit.setError(null);
                        click.setEnabled(true);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final String edit11 = shelf_name_edit.getText().toString();

                    if (TextUtils.isEmpty(edit11)) {
                        shelf_name_edit.setError("Wprowadź adres email.");
                        shelf_name_edit.requestFocus();
                        click.setEnabled(false);
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit11).matches()) {
                        shelf_name_edit.setError("Wprowadź poprawny adres email.");
                        shelf_name_edit.requestFocus();
                        click.setEnabled(false);
                    } else {
                        shelf_name_edit.setError(null);
                        click.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    final String edit11 = shelf_name_edit.getText().toString();

                    if (TextUtils.isEmpty(edit11)) {
                        shelf_name_edit.setError("Wprowadź adres email.");
                        shelf_name_edit.requestFocus();
                        click.setEnabled(false);
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit11).matches()) {
                        shelf_name_edit.setError("Wprowadź poprawny adres email.");
                        shelf_name_edit.requestFocus();
                        click.setEnabled(false);
                    } else {
                        click.setEnabled(true);
                        shelf_name_edit.setError(null);
                    }
                }
            });
            click.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final String edit11 = shelf_name_edit.getText().toString();

                    if (promo_name.equals("bitFlyer")) {

                        final String edit12 = editTextUsernameAccountNumber.getText().toString();
                        String regexStr = "^[0-9]{9,13}$";

                       /* if (TextUtils.isEmpty(edit12)) {
                            editTextUsernameAccountNumber.setError("Wprowadź numer telefonu blik");
                            editTextUsernameAccountNumber.requestFocus();
                           // click.setEnabled(false);
                        } else if (editTextUsernameAccountNumber.toString().length() < 9 || edit12.length() > 13 || !edit12.matches(regexStr)) {
                            editTextUsernameAccountNumber.setError("Wprowadź poprawny numer telefonu.");
                            editTextUsernameAccountNumber.requestFocus();
                            //click.setEnabled(false);
                        } else {

                        */
                            click.setEnabled(true);
                            editTextUsernameAccountNumber.setError(null);
                            if (TextUtils.isEmpty(edit11)) {
                                shelf_name_edit.setError("Wprowadź adres email.");
                                shelf_name_edit.requestFocus();
                                click.setEnabled(false);
                            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit11).matches()) {
                                shelf_name_edit.setError("Wprowadź poprawny adres email.");
                                shelf_name_edit.requestFocus();
                                click.setEnabled(false);
                            } else {
                                Log.i("email from promo", shelf_name_edit.getEditableText().toString());
                                promo_email = shelf_name_edit.getEditableText().toString();
                                if (promo_name.equals("bitFlyer")) {
                                    promo_number_phone = editTextUsernameAccountNumber.getEditableText().toString();
                                    optional = editTextAdditionalInfo.getEditableText().toString();
                                }
                                else{
                                    promo_number_phone = "null";
                                    optional = "null";
                                }
                                String urlAddress2 = "https://yoururl.com/api/insert_done.php";
                                // String done_date = promo.getReleaseDate();

                                String done_date = date_choosen;
                                //Log.i("dzień", String.valueOf( startDay ) );

                                    DoneSender s = new DoneSender(mContext, urlAddress2, String.valueOf(user_id), user_nickname, user_email, String.valueOf(promo_id), promo_name, promo_email, promo_type2, promo_earn, done_date, promo_number_phone, optional);
                                    s.execute();



                                dismiss();

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        PromoDetailActivity.showDialogSuccess();
                                        PromoDetailActivity.updateDonePromo();
                                    }
                                }, 2000);
                            }

                       // }
                    }
                    else{
                        if (TextUtils.isEmpty(edit11)) {
                            shelf_name_edit.setError("Wprowadź adres email.");
                            shelf_name_edit.requestFocus();
                            click.setEnabled(false);
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit11).matches()) {
                            shelf_name_edit.setError("Wprowadź poprawny adres email.");
                            shelf_name_edit.requestFocus();
                            click.setEnabled(false);
                        } else {
                            Log.i("email from promo", shelf_name_edit.getEditableText().toString());
                            promo_email = shelf_name_edit.getEditableText().toString();
                            if (promo_name.equals("bitFlyer")) {
                                promo_number_phone = editTextUsernameAccountNumber.getEditableText().toString();
                                optional = editTextAdditionalInfo.getEditableText().toString();
                            }
                            String urlAddress2 = "https://yoururl.com/api/insert_done.php";
                            // String done_date = promo.getReleaseDate();

                            String done_date = date_choosen;
                            //Log.i("dzień", String.valueOf( startDay ) );

                                DoneSender s = new DoneSender(mContext, urlAddress2, String.valueOf(user_id), user_nickname, user_email, String.valueOf(promo_id), promo_name, promo_email, promo_type2, promo_earn, done_date, null, null);
                                s.execute();


                            dismiss();

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PromoDetailActivity.showDialogSuccess();
                                    PromoDetailActivity.updateDonePromo();
                                }
                            }, 2000);
                        }

                    }

                }
            });

            return view;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    private void display(String toDisplay) {
        //Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();
        date_choosen = toDisplay;

    }
    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((PromoDetailActivity)getActivity()).runSlider();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BottomSheetListener");
        }
    }

}
