using System;
using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using System.Collections.Generic;
using Android.Support.V4.Widget;

namespace LURN_DROID
{
    [Activity(Label = "LURN_DROID", MainLauncher = true, Icon = "@drawable/icon", Theme= "@style/CustomActionBarTheme")]
    public class MainActivity : Activity
    {
        private DrawerLayout m_Drawer;
        private ListView m_DrawerList;
        private static readonly string[] Sections = new[]
        {
            "Dashboard","Friends","Profile"
        };
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);

            ActionBar.SetCustomView(Resource.Layout.action_bar);
            ActionBar.SetDisplayShowCustomEnabled(true);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.Main);

            //this.m_Title = this.m_DrawerTitle = this.Title;
            this.m_Drawer = this.FindViewById<DrawerLayout>(Resource.Id.drawer_layout);
            this.m_DrawerList = this.FindViewById<ListView>(Resource.Id.left_drawer);

            this.m_DrawerList.Adapter = new ArrayAdapter<string>(this, Resource.Layout.item_menu, Sections);
            this.m_DrawerList.ItemClick += DrawerListOnItemClick;
            


        }

        private void DrawerListOnItemClick(object sender, AdapterView.ItemClickEventArgs itemClickEventArgs)
        {
            Android.Support.V4.App.Fragment fragment = null;
            switch (itemClickEventArgs.Position)
            {
                case 0:
                    fragment = new DashboardFragment();
                case 1:
                    fragment = new FriendsFragment();
                case

            }
        }
    }
}

