package cz.muni.fi.pv168.project.zaostan.kalendar.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Peter Stanko
 * @version 2015-02-18
 */
public class Event implements Comparable<Event>
{

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
    private String name;
    private String description;
    private Date dateBegin;
    private Date dateEnd;
    private String address;
    private long id;

    public enum EventType {ALL, CURRENT, UPCOMMING}


    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public Event() {}

    public Event(String name) { this.name = name;}

    public Event(final String name, final String description, final Date dateBegin, final Date dateEnd)
    {
        this.name = name;
        this.description = description;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }


    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Date getDateBegin()
    {
        return dateBegin;
    }
    public String getDateBeginString() { return dateFormat.format(dateBegin); }
    public String getDateEndString() { return dateFormat.format(dateEnd); }

    public void setDateBegin(final Date dateBegin)
    {
        this.dateBegin = new Date(dateBegin.getTime());
    }

    public Date getDateEnd()
    {
        return dateEnd;
    }

    public void setDateEnd(final Date dateEnd)
    {

        this.dateEnd = new Date(dateEnd.getTime());
    }


    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }


    public boolean isNowActive()
    {
        Date now = new Date();
        return now.after(dateBegin) && now.before(dateEnd);
    }

    public boolean isUpcoming()
    {
        Date now = new Date();
        return now.after(dateEnd);
    }


    public int compareTo(final Event o)
    {
        return dateBegin.compareTo(o.getDateBegin());
    }


    @Override
    public String toString()
    {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateBegin=" + dateFormat.format(dateBegin) +
                ", dateEnd=" + dateFormat.format(dateEnd) +
                ", address=" + address +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (!name.equals(event.name)) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (dateBegin != null ? !dateBegin.equals(event.dateBegin) : event.dateBegin != null) return false;
        if (dateEnd != null ? !dateEnd.equals(event.dateEnd) : event.dateEnd != null) return false;
        return !(address != null ? !address.equals(event.address) : event.address != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateBegin != null ? dateBegin.hashCode() : 0);
        result = 31 * result + (dateEnd != null ? dateEnd.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}



