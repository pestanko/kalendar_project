SELECT evts.ID, evts.EVENT_NAME, evts.DESCRIPTION, evts.ADDRESS, evts.DATE_BEGIN, evts.DATE_END
FROM (SELECT EVENT_ID as eid FROM BINDS WHERE USER_ID=?) AS eres
INNER JOIN EVENTS AS evts ON eid=evts.ID