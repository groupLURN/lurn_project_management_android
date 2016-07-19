using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.Locations;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Gms.Maps;
using Android.Gms.Maps.Model;
using Android.Gms.Common;
using Java.Util;

using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android
{
	/// <summary>
	/// "Fragment" for the map - it is required to be an activity although used as a fragment
	/// </summary>
	[Activity (Label = "Map View Fragment", Theme = "@style/CustomHoloTheme")]
	public class MapFragmentActivity : BaseActivity, IOnMapReadyCallback
	{
		
		readonly AssignmentViewModel assignmentViewModel;
		MapView mapView;
		Assignment assignment;
		GoogleMap googleMap;

		public MapFragmentActivity ()
		{
			assignmentViewModel = ServiceContainer.Resolve<AssignmentViewModel> ();
			assignment = assignmentViewModel.SelectedAssignment;
		}

		public void OnMapReady (GoogleMap map)
		{
			googleMap = map;
			googleMap.UiSettings.CompassEnabled = false;
			googleMap.UiSettings.MyLocationButtonEnabled = false;
			googleMap.UiSettings.MapToolbarEnabled = false;

			if (googleMap == null)
				return;

			googleMap.Clear ();

			try {
				MapsInitializer.Initialize (this);
			} catch (GooglePlayServicesNotAvailableException e) {
				Console.WriteLine ("Google Play Services not available:" + e);
				return;
			}

			if (assignment == null)
				return;
			
			var markerOptions = GetMarkerOptionsForAssignment (assignment);
			googleMap.AddMarker (markerOptions).ShowInfoWindow ();
			googleMap.CameraPosition.Target = markerOptions.Position;

			googleMap.MoveCamera (CameraUpdateFactory.NewLatLngZoom (markerOptions.Position, 15f));
		}

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			SetContentView (Resource.Layout.MapFragmentLayout);

			mapView = FindViewById<MapView> (Resource.Id.fragmentMapView);
			mapView.OnCreate (bundle);
			mapView.GetMapAsync (this);
		}

		/// <summary>
		/// Enable location tracking and place any overlays
		/// </summary>
		protected override void OnResume ()
		{
			base.OnResume ();
			mapView.OnResume ();
		}

		/// <summary>
		/// Clear overlays and disable location tracking
		/// </summary>
		protected override void OnPause ()
		{
			base.OnPause ();
			mapView.OnPause ();
		}

		MarkerOptions GetMarkerOptionsForAssignment (Assignment assignment)
		{
			var markerOptions = new MarkerOptions ();
			markerOptions.SetPosition (new LatLng (assignment.Latitude, assignment.Longitude));
			markerOptions.SetTitle (assignment.CompanyName);
			markerOptions.SetSnippet (string.Format ("{0} {1}, {2} {3}",
				assignment.Address, assignment.City, assignment.State, assignment.Zip));

			return markerOptions;
		}
	}
}