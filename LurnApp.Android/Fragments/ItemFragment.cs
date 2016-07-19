using System;
using System.Collections.Generic;
using System.Linq;

using Android.App;
using Android.OS;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android.Fragments
{
	/// <summary>
	/// Fragment for the items section
	/// </summary>
	public class ItemFragment : Fragment
	{

		ListView itemsListView;
		ItemViewModel itemViewModel;

		/// <summary>
		/// The currrent assignment
		/// </summary>
		public Assignment Assignment { get; set; }

		/// <summary>
		/// List of assignment items
		/// </summary>
		public List<AssignmentItem> AssignmentItems { get; set; }

		public override View OnCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			base.OnCreateView (inflater, container, savedInstanceState);

			itemViewModel = ServiceContainer.Resolve<ItemViewModel> ();

			var view = inflater.Inflate (Resource.Layout.ItemsFragmentLayout, null, true);
			itemsListView = view.FindViewById<ListView> (Resource.Id.itemsListViewFragment);
			ReloadAssignmentItems ();
			itemsListView.Enabled = !Assignment.IsHistory;
			return view;
		}

		/// <summary>
		/// Reloads the assignment items in the Listview
		/// </summary>
		void ReloadAssignmentItems ()
		{
			if (AssignmentItems == null)
				return;

			var adapter = new ItemsAdapter (Activity, Resource.Layout.ItemLayout, AssignmentItems);
			adapter.Fragment = this;
			itemsListView.Adapter = adapter;
		}

		/// <summary>
		/// Deletes an AssignmentItem
		/// </summary>
		public void DeleteItem (AssignmentItem item)
		{
			var dialog = new AlertDialog.Builder (Activity).
				SetTitle ("Delete").
				SetMessage ("Are you sure you want to delete this item?").
				SetPositiveButton ("Yes", (sender, e) => {
					itemViewModel.DeleteAssignmentItemAsync (Assignment, item).ContinueWith (_ => Activity.RunOnUiThread (ReloadItems));
				}).SetNegativeButton ("No", (sender, e) => {
			});

			dialog.Show ();
		}

		public void ReloadItems ()
		{
			itemViewModel.LoadAssignmentItemsAsync (Assignment).ContinueWith (_ => {
				Activity.RunOnUiThread (() => {
					AssignmentItems = itemViewModel.AssignmentItems;
					ReloadAssignmentItems ();
					var items = Activity.FindViewById<TextView> (Resource.Id.selectedAssignmentTotalItems);
					items.Text = string.Format ("({0}) Items", Assignment.TotalItems.ToString ());
				});
			});
		}
	}
}