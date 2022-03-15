package networking;

import org.web3j.abi.datatypes.Address;

/** Get this from smart contract? */
public class Account {
    private Address address;

    public Account(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
