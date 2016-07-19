using System;

namespace LurnApp.Android.Utilities {
    /// <summary>
    /// Helper class for making strongly typed EventArgs
    /// </summary>
    public class EventArgs<T> : EventArgs{
        public T Value
        {
            get;
            set;
        }
    }
}