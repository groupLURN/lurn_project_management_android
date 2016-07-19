using Android.App;
using Android.Content;
using Android.Graphics;
using Android.OS;
using Android.Views;
using Android.Widget;

using SignaturePad;

using LurnApp.Android.Fragments;
using LurnApp.Android.Utilities;
using LurnApp.Data;
using LurnApp.Utilities;
using LurnApp.ViewModels;

namespace LurnApp.Android.Dialogs
{
	/// <summary>
	/// Dialog for capturing a signature
	/// </summary>
	public class SignatureDialog : BaseDialog
	{
		readonly Activity activity;
		readonly AssignmentViewModel assignmentViewModel;
		SignaturePadView signatureView;

		/// <summary>
		/// The activity holding this dialog
		/// </summary>
		public Activity Activity { get; set; }

		/// <summary>
		/// The current assignment
		/// </summary>
		public Assignment Assignment { get; set; }

		public SignatureDialog (Activity activity)
			: base (activity)
		{
			this.activity = activity;
			assignmentViewModel = ServiceContainer.Resolve<AssignmentViewModel> ();
		}

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			SetContentView (Resource.Layout.AddSignatureLayout);

			var save = (Button)FindViewById (Resource.Id.signatureSaveButton);
			save.Click += (sender, e) => {
				if (signatureView.IsBlank) {
					AlertDialog.Builder builder = new AlertDialog.Builder (Activity);
					builder.SetTitle (string.Empty).SetMessage ("No signature!").
					SetPositiveButton ("Ok", (innerSender, innere) => {}).Show ();
					return;
				}
				if (assignmentViewModel.Signature == null) {
					assignmentViewModel.Signature = new Signature {
						AssignmentId = Assignment.Id
					};
				}

				assignmentViewModel.Signature.Image = signatureView.GetImage (Color.Black, Color.White).ToByteArray ();
				assignmentViewModel.SaveSignatureAsync ().ContinueWith (_ => {
					activity.RunOnUiThread (() => {
						var fragment = Activity.FragmentManager.FindFragmentById<ConfirmationFragment> (Resource.Id.contentFrame);
						fragment.ReloadConfirmation ();
						Dismiss ();
					});
				});
			};

			var cancel = (Button)FindViewById (Resource.Id.signatureCancelButton);
			cancel.Click += (sender, e) => {
				Dismiss ();
			};

			signatureView = (SignaturePadView)FindViewById (Resource.Id.signatureImage);
			signatureView.BackgroundColor = Color.White;
			signatureView.StrokeColor = Color.Black;
		}
	}
}