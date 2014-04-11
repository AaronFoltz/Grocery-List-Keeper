package com.aaronfoltz.grocerylist.app;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Task")
public class Task extends ParseObject
{
    public Task()
    {

    }

    public boolean isCompleted()
    {
        return getBoolean("completed");
    }

    public void setCompleted(final boolean complete)
    {
        put("completed", complete);
    }

    public String getDescription()
    {
        return getString("description");
    }

    public void setDescription(final String description)
    {
        put("description", description);
    }
}
