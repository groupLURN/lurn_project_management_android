using System.Collections.Generic;

using Android.Content;
using Android.Graphics;
using Android.Views;
using Android.Widget;

using LurnApp.Data;
using LurnApp.Android.Utilities;

namespace LurnApp.Android
{
	/// <summary>
	/// Adapter for a list of photos
	/// </summary>
	public class PhotosAdapter : ArrayAdapter<Photo>
	{
		List<Photo> photos;
		int resourceId;

		public PhotosAdapter (Context context, int resourceId, List<Photo> photos)
			: base (context, resourceId, photos)
		{
			this.photos = photos;
			this.resourceId = resourceId;
		}

		public override View GetView (int position, View convertView, ViewGroup parent)
		{
			Photo photo = null;
			var view = convertView;
			if (view == null) {
				LayoutInflater inflator = (LayoutInflater)Context.GetSystemService (Context.LayoutInflaterService);
				view = inflator.Inflate (resourceId, null);
			}

			if (photos != null && photos.Count > position)
				photo = photos [position];

			if (photo == null)
				return view;

			var image = view.FindViewById<ImageView> (Resource.Id.photoListViewImage);
			var dateTime = view.FindViewById<TextView> (Resource.Id.photoListViewDateTime);
			var description = view.FindViewById<TextView> (Resource.Id.photoListViewDescription);

			dateTime.Text = string.Format ("{0}  {1}", photo.Date.ToString ("t"), photo.Date.ToString ("d"));
			description.Text = photo.Description;
			if (photo.Image != null) {
				var options = new BitmapFactory.Options {
					InScaled = false,
					InDither = false,
					InJustDecodeBounds = false,
					InPurgeable = true,
					InInputShareable = true,
				};
				options.InSampleSize = photo.Image.ToSampleSize ();

				using (var bmp = BitmapFactory.DecodeByteArray (photo.Image, 0, photo.Image.Length, options))
					image.SetImageBitmap (bmp);
			}
			image.Tag = position;

			return view;
		}
	}
}