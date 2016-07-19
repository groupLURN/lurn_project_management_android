using Android.App;
using Android.OS;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;

namespace LurnApp.Android.Fragments
{
	/// <summary>
	/// Fragment for the summary screen
	/// </summary>
	public class SummaryFragment : Fragment
	{
		TextView items, laborhours, expenses, description, descriptionHeader;
		RelativeLayout itemsLayout, expensesLayout, laborHoursLayout;

		/// <summary>
		/// The selected assignment
		/// </summary>
		public Assignment Assignment { get; set; }

		public override View OnCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			base.OnCreateView (inflater, container, savedInstanceState);
			var view = inflater.Inflate (Resource.Layout.SummaryLayout, null, true);

			items = view.FindViewById<TextView> (Resource.Id.summaryAssignmentItems);
			laborhours = view.FindViewById<TextView> (Resource.Id.summaryAssignmentLaborHours);
			expenses = view.FindViewById<TextView> (Resource.Id.summaryAssignmentExpenses);
			description = view.FindViewById<TextView> (Resource.Id.summaryAssignmentDescription);
			descriptionHeader = view.FindViewById<TextView> (Resource.Id.summaryAssignmentDescriptionHeader);
			itemsLayout = view.FindViewById<RelativeLayout> (Resource.Id.summaryItemsLayout);
			laborHoursLayout = view.FindViewById<RelativeLayout> (Resource.Id.summaryLaborLayout);
			expensesLayout = view.FindViewById<RelativeLayout> (Resource.Id.summaryExpensesLayout);

			if (Assignment != null) {
				description.Text = Assignment.Description;
				descriptionHeader.Text = Assignment.CompanyName;
				items.Text = Assignment.TotalItems.ToString ();
				laborhours.Text = Assignment.TotalHours.TotalHours.ToString ("0.0");
				expenses.Text = Assignment.TotalExpenses.ToString ("$#.00");
			}

			if (Assignment != null && !Assignment.IsHistory) {
				itemsLayout.Click += (sender, e) => {
					var index = Constants.Navigation.IndexOf ("Items");
					SelectNavigation (index);
				};
				laborHoursLayout.Click += (sender, e) => {
					var index = Constants.Navigation.IndexOf ("Labor Hours");
					SelectNavigation (index);
				};
				expensesLayout.Click += (sender, e) => {
					var index = Constants.Navigation.IndexOf ("Expenses");
					SelectNavigation (index);
				};
			}

			return view;
		}

		/// <summary>
		/// Call to the navigation fragment to select the correct value in the list view to change fragments
		/// </summary>
		/// <param name="index"></param>
		void SelectNavigation (int index)
		{
			var fragment = Activity.FragmentManager.FindFragmentById<NavigationFragment> (Resource.Id.navigationFragmentContainer);
			fragment.SetNavigation (index);
		}
	}
}