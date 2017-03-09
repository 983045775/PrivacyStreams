package com.github.privacystreams.communication;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.github.privacystreams.core.providers.MultiItemStreamProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanchun on 21/11/2016.
 * a dummy data source
 */

class ContactListProvider extends MultiItemStreamProvider {
    private static final String DESCRIPTION = "dummy data source for testing";

    ContactListProvider() {
        this.addRequiredPermissions(Manifest.permission.READ_CONTACTS);
    }

    @Override
    protected void provide() {
        this.getContactList();
    }

    private void getContactList() {
        ContentResolver contentResolver = this.getContext().getContentResolver();

        Cursor contactCur = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (contactCur != null && contactCur.getCount() > 0) {
            contactCur.moveToFirst();
            while (!contactCur.isAfterLast()) {
                String _id = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Data._ID));
                // The primary display name
                String displayNameKey = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Data.DISPLAY_NAME;
                String name = contactCur.getString(contactCur.getColumnIndex(displayNameKey));
//                String contactID = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
//                String lookup = c.getString(c.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));

                List<String> phones = new ArrayList<>();
                Cursor phoneCur = contentResolver.query(
                        Phone.CONTENT_URI,
                        null,
                        Phone.CONTACT_ID + " = " + _id,
                        null,
                        null);
                while (phoneCur.moveToNext()) {
                    String number = phoneCur.getString(phoneCur.getColumnIndex(Phone.NUMBER));
                    phones.add(number);
//                    int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
                }
                phoneCur.close();

                List<String> emails = new ArrayList<>();
                Cursor emailCur = contentResolver.query(
                        Email.CONTENT_URI,
                        null,
                        Email.CONTACT_ID + " = " + _id,
                        null,
                        null);
                while (emailCur.moveToNext()) {
                    String email = emailCur.getString(emailCur.getColumnIndex(Email.ADDRESS));
                    emails.add(email);
//                    int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
                }
                emailCur.close();

                Contact contact = new Contact(_id, name, phones, emails);
                this.output(contact);
//                Contact contact = new Contact(_id, contactID, lookup, displayName);
//                contactQuery.write(contact);

                contactCur.moveToNext();
            }
        }

        if (contactCur != null) {
            contactCur.close();
        }

        this.finish();
    }
}
