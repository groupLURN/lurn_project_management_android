using System.Collections.Generic;

using Android.Content;
using Android.Views;
using Android.Widget;

using LurnApp.Android.Fragments;
using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android
{
	/// <summary>
	/// Adapter for a list of items
	/// </summary>
	public class ItemsAdapter : ArrayAdapter<AssignmentItem>, View.IOnClickListener
	{

		IList<AssignmentItem> assignmentItems;
		int resourceId;

		public ItemsAdapter (Context context, int resourceId, IList<AssignmentItem> assignmentItems)
			: base (context, resourceId, assignmentItems)
		{
			this.assignmentItems = assignmentItems;
			this.resourceId = resourceId;
		}

		/// <summary>
		/// The parent fragment this adapter is in
		/// </summary>
		public ItemFragment Fragment { get; set; }

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			AssignmentItem item = null;
			var view = convertView;
			if (view == null) {
				LayoutInflater inflator = (LayoutInflater)Context.GetSystemService (Context.LayoutInflaterService);
				view = inflator.Inflate (resourceId, null);
			}

			if (assignmentItems != null && assignmentItems.Count > position)
				item = assignmentItems [position];

			if (item == null)
				return view;

			var name = view.FindViewById<TextView> (Resource.Id.itemName);
			name.Tag = position;

			var trashButton = view.FindViewById<ImageButton> (Resource.Id.itemTrashButton);
			trashButton.SetOnClickListener (this);

			name.Text = string.Format ("#{0} {1}", item.Number, item.Name);
			trashButton.Tag = position;
			trashButton.Focusable = Fragment.Assignment.IsHistory;
			trashButton.Visibility = Fragment.Assignment.IsHistory ? ViewStates.Invisible : ViewStates.Visible;

			return view;
		}

		public void OnClick (View v)
		{
			if (v.Id != Resource.Id.itemTrashButton)
				return;

			var position = (int)v.Tag;
			var item = GetItem (position);
			if (item != null)
				Fragment.DeleteItem (item);
		}
	}
}