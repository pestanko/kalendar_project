package cz.muni.fi.pv168.project.zaostan.kalendar.entities;

/**
 * @author Peter Stanko
 * @version 2015-02-18
 */
public class Bind
{
    private long id = 0;

    private User user;
    private Event event;

    private BindType type = BindType.ATTENDANT;


    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public Bind() {}


    public void setType(BindType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public enum BindType {
        ADMIN, OWNER, ATTENDANT, NONE;

        public static int getBindId(BindType type)
        {
            switch (type)
            {
                case ADMIN:
                    return 1;

                case OWNER:
                    return 2;

                case ATTENDANT:
                    return 10;

            }
        return -1;
        }

        public static BindType getBindType(int id)
        {
            switch (id)
            {
                case 1:
                    return ADMIN;

                case 2:
                    return OWNER;

                case 10:
                    return ATTENDANT;

                default:
                    return NONE;
            }
        }
    }

    public Bind(User user, Event event)
    {
        this.user = user;
        this.event = event;
    }


    @SuppressWarnings("unused")
    public Bind(Event event, User user, BindType type)
    {
        this(user, event);
        this.type = type;
    }


    public Event getEvent()
    {
        return event;
    }

    public User getUser()
    {
        return user;
    }

    public BindType getType() {
        return type;
    }




    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Bind that = (Bind) o;

        return event.equals(that.event) && user.equals(that.user);

    }

    @Override
    public int hashCode()
    {
        int result = user.hashCode();
        result = 31 * result + event.hashCode();
        return result;
    }

    public String toString()
    {
        return String.format("[%s (%s) ;  %s (%s)]", event.getName(), event.getId() , user.getUserName(), user.getId());
    }

}
