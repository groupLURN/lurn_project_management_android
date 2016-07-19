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

namespace LurnApp.Android {
    [Application(Label="Lurn App")]
    public class Application : global::Android.App.Application {

        /// <param name="javaReference">pointer to java</param>
        /// <param name="transfer">transfer enumeration</param>
        public Application (IntPtr javaReference, JniHandleOwnership transfer)
            :base(javaReference, transfer)
        { }

        public override void OnCreate ()
        {
            base.OnCreate ();

            //Registers services for core library
            ServiceRegistrar.Startup ();
        }
    }
}