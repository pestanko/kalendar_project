package cz.muni.fi.pv168.project.zaostan.kalendar.managers;

import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Event;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.User;
import cz.muni.fi.pv168.project.zaostan.kalendar.entities.Bind;
import cz.muni.fi.pv168.project.zaostan.kalendar.exceptions.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Peter Zaoral on 25.02.2015.
 */
public class BindManagerImpl implements BindManager
{
    List<Bind> bindses = new ArrayList<Bind>();
    long index = 0;
    final static Logger logger = LoggerFactory.getLogger(BindManagerImpl.class);
    @Override
    public void addBinding(Bind binding) {
        if (binding == null) return;
        if(binding.getId() != 0) throw new IllegalArgumentException("UserBinding id is not equals zero.");
        logger.debug("Added binding: " + binding);
        binding.setId(++index);
        bindses.add(binding);


    }

    @Override
    public Bind getBinding(long id) throws BindingException {
        return null;
    }

    @Override
    public void removeBinding(long id) throws BindingException
    {
        bindses.remove(id);
    }

    @Override
    public void updateBinding(Bind binding) throws BindingException
    {
        bindses.remove(binding.getId());
        bindses.add(binding);
    }


    @Override
    public List<Event> findEventsWhereIsUser(User user)
    {
        List<Event> results = bindses.stream()
                .filter((Bind binding) -> binding.getUser().equals(user))
                .map(Bind::getEvent)
                .collect(Collectors.toList());
        if(results.size() == 0) {
            logger.info("There is no event where is user: " + user);
            return null;
        }
        return results;
    }

    @Override
    public List<Event> findEventsWhereIsUser(User user, Bind.BindType type) throws BindingException  {
        List<Event> results = bindses.stream()
                .filter((Bind binding) -> binding.getUser().equals(user)
                        && binding.getType().equals(type))
                .map(Bind::getEvent)
                .collect(Collectors.toList());
        if(results.size() == 0) {
            logger.info("Cannot find events where is user: "+ user + " on position: "+ type);
            return null;
        }
        return results;
    }

    @Override
    public List<Event> findUpcommingEvents(User user) throws BindingException  {

        List<Event> result = bindses.stream()
                .filter((Bind bind) -> bind.getUser().equals(user)
                        && bind.getEvent().isUpcoming())
                .map(Bind::getEvent)
                .collect(Collectors.toList());
        if(result.size() == 0) {
            logger.info("There is no upcomming event for "+ user);
            return null;
        }
        return result;
    }

    @Override
    public List<Event> findCurrentEvents(User user) throws BindingException  {

        List<Event> result = bindses.stream()
                .filter((Bind bind) -> bind.getUser().equals(user)
                        && bind.getEvent().isNowActive())
                .map(Bind::getEvent)
                .collect(Collectors.toList());
        if(result.size() == 0) {
            logger.info("There is no current event for " + user);
            return null;
        }
        return result;
    }

    @Override
    public List<User> findUsersInEvent(Event event) throws BindingException
    {
        List<User> results = bindses.stream()
                .filter((Bind binding)-> binding.getEvent().equals(event))
                .map(Bind::getUser)
                .collect(Collectors.toList());
        if(results.size() == 0) {
            logger.info("There is no user in event: " + event);
            return null;
        }
        return results;
    }

    @Override
    public List<Bind> getAllBindings() {
        return null;
    }

    public List<Bind> getBingings() throws BindingException  { return bindses;}



}
