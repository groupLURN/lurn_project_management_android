
using System;
using System.ComponentModel;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Util;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android
{
	/// <summary>
	/// Activity for the tab bar between "Assignments" and "Map Overview"
	/// </summary>
	[Activity (Label = "Assignment Tabs", Theme = "@style/CustomHoloTheme")]
	public class AssignmentTabActivity : BaseActivity
	{
		LocalActivityManager localManger;
		TabHost tabHost;

		public TabHost TabHost {
			get {
				return tabHost;
			}
		}

		public MapDataWrapper MapData { get; set; }

		public AssignmentTabActivity ()
		{
			ServiceContainer.Register<ISynchronizeInvoke> (() => new SynchronizeInvoke { Activity = this });
		}

		protected override void OnCreate (Bundle savedInstanceState)
		{
			base.OnCreate (savedInstanceState);

			SetContentView (Resource.Layout.AssignmentsTabsLayout);

			tabHost = FindViewById<TabHost> (Resource.Id.assingmentTabHost);
			//In order to use tabs outside of a TabActivity I have to use this local activity manager and dispatch create the savedInstanceState
			localManger = new LocalActivityManager (this, true);
			localManger.DispatchCreate (savedInstanceState);
			tabHost.Setup (localManger);

			TabHost.TabSpec assignmentsSpec = tabHost.NewTabSpec ("list");
			Intent assignmentIntent = new Intent (this, typeof(AssignmentsActivity));
			assignmentsSpec.SetContent (assignmentIntent);
			assignmentsSpec.SetIndicator ("list");

			TabHost.TabSpec mapViewSpec = tabHost.NewTabSpec ("map");
			Intent mapViewIntent = new Intent (this, typeof(MapViewActivity));
			mapViewSpec.SetContent (mapViewIntent);
			mapViewSpec.SetIndicator ("map");

			tabHost.AddTab (assignmentsSpec);
			tabHost.AddTab (mapViewSpec);

			tabHost.TabChanged += (sender, e) => {
				if (tabHost.CurrentTab == 0)
					MapData = null;
			};

			try {
				if (savedInstanceState != null) {
					if (savedInstanceState.ContainsKey (Constants.CurrentTab)) {
						var currentTab = savedInstanceState.GetInt (Constants.CurrentTab, 0);
						tabHost.CurrentTab = currentTab;
					} else {
						tabHost.CurrentTab = 0;
					}

					MapData = savedInstanceState.ContainsKey ("mapData") ?
						(MapDataWrapper)savedInstanceState.GetSerializable ("mapData") : null;
				} else {
					MapData = null;
					tabHost.CurrentTab = 0;
				}
			} catch (Exception) {
				tabHost.CurrentTab = 0;
			}
		}

		protected override void OnResume ()
		{
			//have to clean up the local activity manager in on resume.
			localManger.DispatchResume ();
			base.OnResume ();
		}

		protected override void OnPause ()
		{
			//have to clean up the local activity manager in on pause.
			localManger.DispatchPause (IsFinishing);
			base.OnPause ();
		}

		protected override void OnStop ()
		{
			//have to clean up the local activity manager in on stop.
			localManger.DispatchStop ();
			base.OnStop ();
		}

		protected override void OnSaveInstanceState (Bundle outState)
		{
			localManger.SaveInstanceState ();
			outState.PutSerializable ("mapData", MapData);
			outState.PutInt (Constants.CurrentTab, (int)tabHost.CurrentTab);
			base.OnSaveInstanceState (outState);
		}

		public class MapDataWrapper : Java.Lang.Object, Java.IO.ISerializable
		{
			public int Zoom { get; set; }

			public View OverlayBubble { get; set; }
		}
	}
}