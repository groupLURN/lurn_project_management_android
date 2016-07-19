using System.Collections.Generic;
using System.Linq;

using Android.Content;
using Android.Graphics;
using Android.Views;
using Android.Widget;

using LurnApp.Data;
using LurnApp.Utilities;

namespace LurnApp.Android
{
	/// <summary>
	/// Adapter for a spinner for selecting LaborType
	/// </summary>
	public class LaborTypeSpinnerAdapter : BaseAdapter, ISpinnerAdapter
	{
		List<string> items;
		Context context;
		int resourceId;

		public override int Count {
			get {
				return items.Count;
			}
		}

		/// <summary>
		/// Text color for the item
		/// </summary>
		public Color TextColor { get; set; }

		/// <summary>
		/// Background color of the text view
		/// </summary>
		public Color Background { get; set; }

		public LaborTypeSpinnerAdapter (LaborType[] items, Context context, int resourceId)
			: base ()
		{
			this.items = new List<string> ();
			foreach (var item in items)
				this.items.Add (item.ToUserString ());

			this.context = context;
			this.resourceId = resourceId;
		}

		public override Java.Lang.Object GetItem (int position)
		{
			return items [position];
		}

		public override long GetItemId (int position)
		{
			return (long)position;
		}

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			var view = convertView;
			if (view == null) {
				var inflator = (LayoutInflater)context.GetSystemService (Context.LayoutInflaterService);
				view = inflator.Inflate (resourceId, parent, false);
			}

			var item = items.ElementAtOrDefault (position);
			if (item == null)
				return view;

			var textView = view.FindViewById<TextView> (Resource.Id.simpleSpinnerTextView);
			textView.Text = item;
			textView.SetTextColor (TextColor);
			textView.SetBackgroundColor (Background);

			return view;
		}

		protected override void Dispose (bool disposing)
		{
			context = null;
			base.Dispose (disposing);
		}
	}
}