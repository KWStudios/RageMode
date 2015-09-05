# Contributing
You readed through our code and thought "What a mess, I could do this 1000 times more efficient" or you want to add a new feature?
Then you are welcome to contribute to our project.

First of all, Fork this repository by clicking the *Fork* button on GitHub. You can then clone your fork within eclipse and 
change everything you want to change or add the new features you want to add.

## While adding your code, please follow these simple rules to make our lives easier
* Avoid removing our code, also if it is just a comment or a warning from eclipse, because if there is a warning and we don't remove it, we have done this on purpose. If you find a bug which you can fix by removing our code, you can do it of course :)
* Never remove something if you don't know what you are doing.
* If your work is done, hit [Ctrl + Shift + F] inside all files which you have edited to format it correctly.

For example this code would not be acceptable:

```java
List<ExampleObject> myList = new List<ExampleObject>();
for(ExampleObject eO:myList){
doMyWork();
}
if(someBoolean){
if(someOtherBoolean){
if(thirdBoolean){
if(string.equals("test"){
doSomeOtherWork();
}
}
}
}
```

It should look like that to be acceptable:

```java
List<ExampleObject> myList = new List<ExampleObject>();
for(ExampleObject eO : myList){
    doMyWork();
}

if(someBoolean){
    if(someOtherBoolean){
        if(thirdBoolean){
            if(string.equals("test"){
                doSomeOtherWork();
            }
        }
    }
}
```

* Before creating a pull request, organize all imports by right clicking your code and hitting *Source > Organize imports*

Something like this would not be acceptable:

```java
import java.util.List;
import org.bukkit.Location;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Arrays;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
```

It should look like this, sorted and organized:

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
```

* Avoid creating pull requests before testing your changes. You should always test your code at least once before creating a pull request.
If we encounter many problems with your code, your next pull requests may not be accepted.

## Create the pull request

If you followed these rules during your work, your pull request will likely be accepted.  
Just create a pull request for your fork on the original [repository site](https://github.com/KWStudios/RageMode).  
If you are unhappy with our rules, create a new [issue](https://github.com/KWStudios/RageMode/issues) and ask what you want to change.  
If you want to be named in the credits, just leave a comment inside our code near to your changes and we will name you on the Spigot Wiki.
