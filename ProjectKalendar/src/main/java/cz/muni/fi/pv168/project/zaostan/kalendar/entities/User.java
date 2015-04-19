package cz.muni.fi.pv168.project.zaostan.kalendar.entities;

/**
 * @author Peter Stanko
 * @version 2015-02-18
 */
public class User implements Comparable<User>
{
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String mobileNumber;
    private long id;

    private String address;


    public class Builder
    {
        public User user;
        public Builder()
        {
            user = new User();
        }

        public Builder name(String name)
        {
            user.setFirstName(name);
            return this;
        }

        public Builder surname(String name)
        {
            user.setLastName(name);
            return this;
        }

        public Builder userName(String username)
        {
            user.setUserName(username);
            return this;
        }

        public Builder email(String email)
        {
            user.setEmail(email);
            return this;
        }

        public Builder mobile(String number)
        {
            user.setMobileNumber(number);
            return this;
        }

        public Builder id(long id)
        {
            user.setId(id);
            return this;
        }

        public Builder address(String address)
        {
            user.setAddress(address);
            return this;
        }

        public User build()
        {
            return user;
        }

    }


    public User() {}

    public User(final String firstName, final String surname, final String userName, final String email, final long id)
    {
        this.firstName = firstName;
        this.lastName = surname;
        this.userName = userName;
        this.email = email;
        this.id = id;
    }

    public User(final String firstName, final String surname, final String userName, final String email)
    {
        this(firstName, surname, userName, email, 0);

    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }


    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(final String address)
    {
        this.address = address;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public int compareTo(final User o)
    {
        return Long.compare(o.getId(), this.getId());
    }



    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", address='"+ address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (!lastName.equals(user.lastName)) return false;
        if (!userName.equals(user.userName)) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(user.mobileNumber) : user.mobileNumber != null) return false;
        return !(address != null ? !address.equals(user.address) : user.address != null);

    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
