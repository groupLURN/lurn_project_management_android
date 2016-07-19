using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace LurnApp.Android.Dialogs
{
	/// <summary>
	/// Base dialog class, mainly for setting the CustomDialogTheme
	/// </summary>
	public class BaseDialog : Dialog
	{
		public BaseDialog (Context context)
			: base (context, Resource.Style.CustomDialogTheme)
		{

		}
	}
}