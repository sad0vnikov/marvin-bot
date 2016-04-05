package contact;


public abstract class ContactManager {

    public Contact getContactByName(String name) {
        return new Contact(this, name);
    }
}
