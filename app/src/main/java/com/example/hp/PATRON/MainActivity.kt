package com.example.hp.PATRON

import android.content.ClipData
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.example.hp.PATRON.contacts.showphonecontacts;
import com.example.hp.PATRON.contacts.showdbcontacts;
import com.example.hp.PATRON.webcontacts.showwebcontacts
import com.example.hp.PATRON.feedback.guest


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    lateinit var subitem1:MenuItem
    lateinit var subitem2:MenuItem
    lateinit var feedmenu:MenuItem
    override fun onDrawerOpened(drawerView: View?) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDrawerStateChanged(newState: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDrawerClosed(drawerView: View?) {
        subitem1.setVisible(false)
        subitem2.setVisible(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
       val m= findViewById<NavigationView>(R.id.nav_view).menu
       subitem1= m.findItem(R.id.feedbackdetail)
        subitem2=m.findItem(R.id.byday)
        feedmenu=m.findItem(R.id.feedback)

        val db=com.example.hp.PATRON.dbconnect.DBHelper(this)
        if(db.getkeysavilable()==true) {
            if (db.getloginavilable() == true) {
                val i = Intent(this, com.example.hp.PATRON.pinlogin::class.java)

                startActivity(i)
            }
            else
            {

                val i = Intent(this, com.example.hp.PATRON.nowlogin::class.java)

                startActivity(i)
            }
        }
        else
        {
            val i = Intent(this, com.example.hp.PATRON.serial::class.java)

            startActivity(i)
        }



         val log = homescreen()
        val transaction = supportFragmentManager.beginTransaction().detach(log).attach(log)
        transaction.replace(R.id.removable, log, "homescreen")
        transaction.commit()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Send direct message", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            val fragment = com.example.hp.PATRON.contacts.massage.compose()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.removable, fragment)
            transaction.commit()

        }
        fab1.setOnClickListener { view ->
            Snackbar.make(view, "Work in progress", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()


        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()
       // drawer_layout.addDrawerListener(this)
        nav_view.setNavigationItemSelectedListener(this)


        }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else
        {
            val myFragment = supportFragmentManager.findFragmentByTag("homescreen")
//            System.out.println("this is frgmsg"+myFragment.isAdded)
            if (myFragment !=null &&myFragment.isVisible) {
             System.out.println("we are ready to finish")
            this.finish();
            }
            else {
                val log = homescreen()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable, log, "homescreen")
                transaction.commit()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu):Boolean {
        var editor: SharedPreferences.Editor
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        if(sharedPref.getString("popup","").equals("true"))
        {
            menu.findItem(R.id.action_autosave).setTitle("Stop Autosave")
        }
        else if(sharedPref.getString("popup","").equals("false"))
        {
            menu.findItem(R.id.action_autosave).setTitle("Start Autosave")
        }

        if(fab.isShown)
        {
            menu.findItem(R.id.action_fab).setTitle("Hide Favourate Icon")
        }
        else
        {
            menu.findItem(R.id.action_fab).setTitle("Show Favourate Icon")
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {

            R.id.action_settings ->{
            val i = Intent(this, com.example.hp.PATRON.settings.setdefaultgroup::class.java)

            startActivity(i)
            return true;
        }
            R.id.action_shortcutsetting ->{
               val short=com.example.hp.PATRON.settings.shortcutsetting(this)
                return true;
            }
            R.id.action_fab ->{

               if(fab.isShown)
               {
                   fab.hide()
                   fab1.hide()
               invalidateOptionsMenu()
               }
                else
               {
                   fab.show()
                   fab1.show()
                   invalidateOptionsMenu()
               }

                return true;
            }
            R.id.action_autosave ->{
                 var editor: SharedPreferences.Editor
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                editor = sharedPref.edit()
                val state = sharedPref.getString("popup", "")
                println("this is state: " + state)
                if (state.equals("true", ignoreCase = true)) {
                    editor.putString("popup", "false")
                    editor.commit()
                } else {
                    editor.putString("popup", "true")
                    editor.commit()

            }
                return true;
            }
            R.id.action_logout ->{
                val i = Intent(this, com.example.hp.PATRON.pinlogin::class.java)

                startActivity(i)
                return true;
            }
            R.id.action_removeaccount ->{
                val i = Intent(this, com.example.hp.PATRON.nowlogin::class.java)

                startActivity(i)
                return true;
            }
            R.id.action_removelicense->{
                val i = Intent(this, com.example.hp.PATRON.serial::class.java)

                startActivity(i)
                return true;
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.DBcontacts -> {
                val log=showdbcontacts()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable,log)
                transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.Phonecontacts -> {
                val log=showphonecontacts()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable,log)
                transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.onlinecontacts -> {
                val fragment = showwebcontacts()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable, fragment)
                transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.towebbook -> {

                    val fragment = com.example.hp.PATRON.webcontacts.massage.compose()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.removable, fragment)
                    transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)

            }
            R.id.tophonebook -> {

                val fragment = com.example.hp.PATRON.contacts.massage.compose()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable, fragment)
                transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)

            }
            R.id.help -> {

            }
            R.id.feedback -> {
                if(subitem1.isVisible) {
                    subitem1.setVisible(false)

              subitem2.setVisible(false)
                    feedmenu.setTitle("Feedback                      ▶")

                }
                else
                {
                    feedmenu.setTitle("Feedback                      ▼")
                    subitem1.setVisible(true)
                    subitem2.setVisible(true)
                }
            }
            R.id.feedbackdetail -> {
                val fragment = guest()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable, fragment)
                transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.byday -> {
                val fragment = com.example.hp.PATRON.feedback.getguestdetails()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.removable, fragment)
                transaction.commit()
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
        return true
    }


}
