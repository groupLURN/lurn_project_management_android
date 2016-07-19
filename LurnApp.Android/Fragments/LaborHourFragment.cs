using System.Linq;
using System.Collections.Generic;

using Android.App;
using Android.OS;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Dialogs;
using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android.Fragments
{
	/// <summary>
	/// Fragment for the labor hours section
	/// </summary>
	public class LaborHourFragment : Fragment
	{
		ListView laborListView;
		AddLaborDialog laborDialog;
		LaborViewModel laborViewModel;

		/// <summary>
		/// The current assignment
		/// </summary>
		public Assignment Assignment { get; set; }

		/// <summary>
		/// The list of labor hours
		/// </summary>
		public List<Labor> LaborHours { get; set; }

		public override View OnCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			base.OnCreateView (inflater, container, savedInstanceState);
			var view = inflater.Inflate (Resource.Layout.LaborHoursLayout, null, true);

			laborViewModel = ServiceContainer.Resolve<LaborViewModel> ();

			laborListView = view.FindViewById<ListView> (Resource.Id.laborListViewFragment);

			ReloadLaborHours ();
			laborListView.ItemClick += (sender, e) => {
				var textView = e.View.FindViewById<TextView> (Resource.Id.laborHours);

				var labor = LaborHours.ElementAtOrDefault ((int)textView.Tag);

				laborDialog = new AddLaborDialog (Activity);
				laborDialog.Assignment = Assignment;
				laborDialog.CurrentLabor = labor;
				laborDialog.Show ();
			};
			return view;
		}

		/// <summary>
		/// Reload the view in the listview by itself without calling to reload the list.
		/// </summary>
		/// <param name="index">index of the list view item to reload</param>
		public void ReloadSingleListItem (int index)
		{
			if (laborListView.FirstVisiblePosition < index && index < laborListView.LastVisiblePosition) {
				var view = laborListView.GetChildAt (index);
				if (view != null) {
					laborListView.Adapter.GetView (index, view, laborListView);
				}
			}
		}

		/// <summary>
		/// Reloads the labor hours in the ListView
		/// </summary>
		void ReloadLaborHours ()
		{
			if (LaborHours == null)
				return;

			var adapter = new LaborHoursAdapter (Activity, Resource.Layout.LaborHoursListItemLayout, LaborHours);
			adapter.Fragment = this;
			adapter.Assignment = Assignment;
			laborListView.Adapter = adapter;
		}

		/// <summary>
		/// Reloads the labor hours from the view model
		/// </summary>
		public void ReloadHours ()
		{
			laborViewModel.LoadLaborHoursAsync (Assignment).ContinueWith (_ => {
				Activity.RunOnUiThread (() => {
					LaborHours = laborViewModel.LaborHours;
					ReloadLaborHours ();
					var items = Activity.FindViewById<TextView> (Resource.Id.selectedAssignmentTotalItems);
					items.Text = string.Format ("{0} hrs", Assignment.TotalHours.TotalHours.ToString ("0.0"));
				});
			});
		}

		/// <summary>
		/// Dismiss any child dialogs
		/// </summary>
		public override void OnPause ()
		{
			base.OnPause ();
			if (laborDialog != null && laborDialog.IsShowing)
				laborDialog.Dismiss ();
		}
	}
}