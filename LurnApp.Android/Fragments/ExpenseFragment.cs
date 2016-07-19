using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Dialogs;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;
using LurnApp.Android.Utilities;

namespace LurnApp.Android.Fragments
{
	public class ExpenseFragment : Fragment
	{
		ListView expensesListView;
		ExpenseDialog expenseDialog;
		ExpenseViewModel expenseViewModel;

		public List<Expense> Expenses { get; set; }

		/// <summary>
		/// selected assignment
		/// </summary>
		public Assignment Assignment { get; set; }

		public override View OnCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			base.OnCreateView (inflater, container, savedInstanceState);

			expenseViewModel = ServiceContainer.Resolve<ExpenseViewModel> ();

			var view = inflater.Inflate (Resource.Layout.ExpensesFragmentLayout, null, true);

			expensesListView = view.FindViewById<ListView> (Resource.Id.expenseListView);

			ReloadExpenses ();
			expensesListView.ItemClick += (sender, e) => {
				var textView = e.View.FindViewById<TextView> (Resource.Id.expenseText);

				var expense = Expenses.ElementAtOrDefault ((int)textView.Tag);

				expenseDialog = new ExpenseDialog (Activity);
				expenseDialog.Assignment = Assignment;
				expenseDialog.CurrentExpense = expense;
				expenseDialog.Show ();
			};

			return view;
		}

		/// <summary>
		/// Reload the view in the listview by itself without calling to reload the list.
		/// </summary>
		/// <param name="index">index of the list view item to reload</param>
		void ReloadSingleListItem (int index)
		{
			if (expensesListView.FirstVisiblePosition < index && index < expensesListView.LastVisiblePosition) {
				var view = expensesListView.GetChildAt (index);
				if (view != null)
					expensesListView.Adapter.GetView (index, view, expensesListView);
			}
		}

		/// <summary>
		/// Reloads the expense in the list view
		/// </summary>
		void ReloadExpenses ()
		{
			if (Expenses != null)
				expensesListView.Adapter = new ExpensesAdapter (Activity, Resource.Layout.ExpenseListItemLayout, Expenses);
		}

		/// <summary>
		/// Reloads the expenses from the view model
		/// </summary>
		public void ReloadExpenseData ()
		{
			expenseViewModel.LoadExpensesAsync (Assignment).ContinueWith (_ => {
				Activity.RunOnUiThread (() => {
					Expenses = expenseViewModel.Expenses;
					ReloadExpenses ();
					var items = Activity.FindViewById<TextView> (Resource.Id.selectedAssignmentTotalItems);
					items.Text = Assignment.TotalExpenses.ToString ("$0.00");
				});
			});
		}
	}
}