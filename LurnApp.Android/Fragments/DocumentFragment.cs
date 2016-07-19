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
using Uri = Android.Net.Uri;

using Java.IO;

using LurnApp.Android.Adapters;
using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android.Fragments
{
	public class DocumentFragment : Fragment
	{
		ListView documentListView;
		File file;

		public List<Document> Documents { get; set; }

		public override View OnCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			base.OnCreateView (inflater, container, savedInstanceState);
			var view = inflater.Inflate (Resource.Layout.DocumentFragmentLayout, null, true);

			documentListView = view.FindViewById<ListView> (Resource.Id.documentsListView);
			if (Documents != null)
				documentListView.Adapter = new DocumentsAdapter (Activity, Resource.Layout.DocumentListItemLayout, Documents);

			documentListView.ItemClick += (sender, e) => {
				var textView = e.View.FindViewById<TextView> (Resource.Id.documentListItemDocTitle);

				var document = Documents.ElementAtOrDefault ((int)textView.Tag);

				//start intent with the uri path of the document
				var strings = document.Path.Split ('/');
				CopyReadAsset (strings [1]);
				var intent = new Intent (Intent.ActionView);
				var uri = Uri.FromFile (file);
				intent.SetDataAndType (uri, "application/pdf");
				intent.SetFlags (ActivityFlags.ClearTop);
				try {
					Activity.StartActivity (intent);
				} catch (ActivityNotFoundException exc) {
					Log.WriteLine (LogPriority.Error, Constants.LogTag, exc.Message);
				}
			};

			return view;
		}

		/// <summary>
		/// Helper function to copy the pdf to the android file system for viewing.
		/// </summary>
		/// <param name="fileName"></param>
		void CopyReadAsset (string fileName)
		{
			file = new File (Activity.FilesDir, Constants.PdfFile);
			try {
				using (var input = Activity.Assets.Open (fileName))
					using (var output = Activity.OpenFileOutput (file.Name, FileCreationMode.WorldReadable))
						input.CopyTo (output);
			} catch (Exception e) {
				Log.WriteLine (LogPriority.Error, Constants.LogTag, e.StackTrace);
			}
		}
	}
}