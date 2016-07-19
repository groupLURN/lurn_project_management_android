using Android.App;
using Android.Content;
using Android.OS;
using Android.Views;

using LurnApp.Android.Utilities;

namespace LurnApp.Android.Fragments
{
	/// <summary>
	/// Fragment for the maps section
	/// </summary>
	public class MapFragment : Fragment
	{
		LocalActivityManager localManager;

		public override void OnCreate (Bundle savedInstanceState)
		{
			base.OnCreate (savedInstanceState);

			//Like the tabs issue, we have to create a local activity manager to enable us to use a mapview outside of mapactivity.
			//credits go to BahaiResearch.com on stackoverflow http://stackoverflow.com/questions/5109336/mapview-in-a-fragment-honeycomb
			localManager = new LocalActivityManager (Activity, true);
			localManager.DispatchCreate (savedInstanceState);
		}

		public override View OnCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			var intent = new Intent (Activity, typeof(MapFragmentActivity));
			//pass the index of the assignment through to the actual map activity
			var window = localManager.StartActivity ("MapFragmentActivity", intent);
			View currentView = window.DecorView;
			currentView.Visibility = ViewStates.Visible;
			currentView.FocusableInTouchMode = true;
			((ViewGroup)currentView).DescendantFocusability = DescendantFocusability.AfterDescendants;
			return currentView;
		}

		/// <summary>
		/// Resume the LocalActivityManager
		/// </summary>
		public override void OnResume ()
		{
			base.OnResume ();
			localManager.DispatchResume ();
		}

		/// <summary>
		/// Pause the LocalActivityManager
		/// </summary>
		public override void OnPause ()
		{
			base.OnPause ();
			localManager.DispatchPause (Activity.IsFinishing);
		}

		/// <summary>
		/// Stop the LocalActivityManager
		/// </summary>
		public override void OnStop ()
		{
			base.OnStop ();
			localManager.DispatchStop ();
		}
	}
}