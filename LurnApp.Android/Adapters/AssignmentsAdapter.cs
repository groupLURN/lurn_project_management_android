using System.Collections.Generic;
using System.Linq;

using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;
using Extensions = LurnApp.Android.Utilities.AndroidExtensions;
using Orientation = Android.Content.Res.Orientation;

namespace LurnApp.Android
{
	/// <summary>
	/// Adapter for a list of assignments
	/// </summary>
	public class AssignmentsAdapter : ArrayAdapter<Assignment>, View.IOnClickListener, AdapterView.IOnItemSelectedListener
	{

		readonly AssignmentViewModel assignmentViewModel;
		readonly AssignmentsActivity activity;
		readonly MenuViewModel menuViewModel;
		readonly IList<Assignment> assignments;
		readonly int resourceId;

		public AssignmentsAdapter (AssignmentsActivity activity, int resourceId, List<Assignment> assignments)
			: base (activity, resourceId, assignments)
		{
			assignmentViewModel = ServiceContainer.Resolve<AssignmentViewModel> ();
			menuViewModel = ServiceContainer.Resolve<MenuViewModel> ();

			this.activity = activity;
			this.assignments = assignments;
			this.resourceId = resourceId;
		}

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			Assignment assignment = null;
			var view = convertView;
			if (view == null) {
				LayoutInflater inflator = (LayoutInflater)Context.GetSystemService (Context.LayoutInflaterService);
				view = inflator.Inflate (resourceId, null);
			}

			if (assignments.Count > position)
				assignment = assignments [position];

			if (assignment == null)
				return view;

			var number = view.FindViewById<TextView> (Resource.Id.assignmentItemNumber);
			var job = view.FindViewById<TextView> (Resource.Id.assignmentJob);
			var name = view.FindViewById<TextView> (Resource.Id.assignmentName);
			var phone = view.FindViewById<TextView> (Resource.Id.assignmentPhone);
			var address = view.FindViewById<TextView> (Resource.Id.assignmentAddress);
			var buttonLayout = view.FindViewById<LinearLayout> (Resource.Id.assignmentButtonLayout);
			var timerLayout = view.FindViewById<LinearLayout> (Resource.Id.assignmentTimerLayout);
			var timerlinearLayout = view.FindViewById<LinearLayout> (Resource.Id.timerLinearLayout);
			var spinner = view.FindViewById<Spinner> (Resource.Id.assignmentStatus);
			var spinnerImage = view.FindViewById<ImageView> (Resource.Id.assignmentStatusImage);
			var accept = view.FindViewById<Button> (Resource.Id.assignmentAccept);
			var decline = view.FindViewById<Button> (Resource.Id.assignmentDecline);
			var phoneButton = view.FindViewById<RelativeLayout> (Resource.Id.assignmentPhoneLayout);
			var mapButton = view.FindViewById<RelativeLayout> (Resource.Id.assignmentAddressLayout);

			mapButton.SetOnClickListener (this);
			phoneButton.SetOnClickListener (this);
			accept.SetOnClickListener (this);
			decline.SetOnClickListener (this);

			accept.Tag = position;
			decline.Tag = position;
			mapButton.Tag = position;

			if (assignment.Status == AssignmentStatus.New) {
				buttonLayout.Visibility = ViewStates.Visible;
				timerLayout.Visibility = ViewStates.Gone;
			} else {
				buttonLayout.Visibility = ViewStates.Gone;
				timerLayout.Visibility = ViewStates.Visible;
				timerlinearLayout.Visibility = Context.Resources.Configuration.Orientation == Orientation.Landscape ? ViewStates.Invisible : ViewStates.Gone;

				spinner.Focusable = false;
				spinner.Tag = position;
				var adapter = new SpinnerAdapter<AssignmentStatus> (assignmentViewModel.AvailableStatuses, Context, Resource.Layout.SimpleSpinnerItem);
				adapter.TextColor = Context.Resources.GetColor (Resource.Color.greyspinnertext);
				adapter.Background = Color.White;
				spinner.Adapter = adapter;

				spinner.SetSelection (assignmentViewModel.AvailableStatuses.ToList ().IndexOf (assignment.Status));
				spinner.SetBackgroundResource (Resource.Drawable.trianglewhite);
				spinnerImage.SetImageResource (Resource.Drawable.HoldImage);

				spinner.OnItemSelectedListener = this;
			}

			number.Text = assignment.Priority.ToString ();
			job.Text = string.Format ("#{0} {1}\n{2}", assignment.JobNumber, assignment.StartDate.ToShortDateString (), assignment.CompanyName);
			name.Text = assignment.ContactName;
			phone.Text = assignment.ContactPhone;
			address.Text = string.Format ("{0}\n{1}, {2} {3}", assignment.Address, assignment.City, assignment.State, assignment.Zip);

			return view;
		}

		/// <summary>
		/// Save assignment to the view model.
		/// </summary>
		async void SaveAssignment (Assignment assignment, int index)
		{
			await assignmentViewModel.SaveAssignmentAsync (assignment);
			activity.RunOnUiThread (() => {
				if (assignment.Status == AssignmentStatus.Active || assignment.Status == AssignmentStatus.Declined)
					activity.ReloadAssignments ();
				else
					activity.ReloadSingleListItem (index);
			});
		}

		public void OnClick (View v)
		{
			var position = (int)v.Tag;
			var activeAssignment = GetItem (position);

			switch (v.Id) {
			case Resource.Id.assignmentAccept:
				activeAssignment.Status = assignmentViewModel.ActiveAssignment == null ?
					AssignmentStatus.Active : AssignmentStatus.Hold;
				SaveAssignment (activeAssignment, position);
				break;
			case Resource.Id.assignmentDecline:
				activeAssignment.Status = AssignmentStatus.Declined;
				SaveAssignment (activeAssignment, position);
				break;
			case Resource.Id.assignmentPhoneLayout:
				var view = (View)v.Parent;
				var phone = view.FindViewById<TextView> (Resource.Id.assignmentPhone);
				var activity = this.activity;
				activity.MakePhoneCall (phone.Text);
				break;
			case Resource.Id.assignmentAddressLayout:
				var intent = new Intent (Context, typeof(SummaryActivity));
				var tabActivity = (AssignmentTabActivity)this.activity.Parent;
				tabActivity.MapData = null;
				assignmentViewModel.SelectedAssignment = activeAssignment;
				menuViewModel.MenuIndex = Constants.Navigation.IndexOf ("Map");
				Context.StartActivity (intent);
				break;
			}
		}

		public void OnItemSelected (AdapterView parent, View view, int position, long id)
		{
			var selected = assignmentViewModel.AvailableStatuses.ElementAtOrDefault (position);
			var spinnerImage = ((View)parent.Parent).FindViewById<ImageView> (Resource.Id.assignmentStatusImage);
			var index = (int)parent.Tag;
			var activeAssignment = GetItem (index);
			if (activeAssignment.Status == selected)
				return;
			
			switch (selected) {
			case AssignmentStatus.Active:
				spinnerImage.SetImageResource (Resource.Drawable.EnrouteImage);
				spinnerImage.InvalidateDrawable (spinnerImage.Drawable);
				activeAssignment.Status = AssignmentStatus.Active;
				SaveAssignment (activeAssignment, index);
				break;
			case AssignmentStatus.Complete:
				//go to confirmations
				activeAssignment.Status = selected;
				var activity = (AssignmentTabActivity)this.activity.Parent;
				var intent = new Intent (activity, typeof(SummaryActivity));
				menuViewModel.MenuIndex = Constants.Navigation.IndexOf (Constants.Confirmations);
				assignmentViewModel.SelectedAssignment = activeAssignment;
				activity.StartActivity (intent);
				break;
			default:
				spinnerImage.SetImageResource (Resource.Drawable.HoldImage);
				spinnerImage.InvalidateDrawable (spinnerImage.Drawable);
				parent.SetBackgroundResource (Resource.Drawable.trianglewhite);
				activeAssignment.Status = selected;
				SaveAssignment (activeAssignment, index);
				break;
			}
		}

		public void OnNothingSelected (AdapterView parent)
		{
		}
	}
}