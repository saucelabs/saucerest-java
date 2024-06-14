package com.saucelabs.saucerest.model.accounts;

public class Links {

    public Object next;
    public Object previous;
    public String first;
    public String last;

    /**
     * No args constructor for use in serialization
     */
    public Links() {
    }

    /**
     * @param next
     * @param previous
     * @param last
     * @param first
     */
    public Links(Object next, Object previous, String first, String last) {
        super();
        this.next = next;
        this.previous = previous;
        this.first = first;
        this.last = last;
    }

}
