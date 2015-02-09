package org.bu.android.widget.dragexplist;

import org.bu.android.widget.BuSlideView;

public interface BuListItemMaster {

	public class BuListItem {
		private BuSlideView buSlideView = null;

		public BuSlideView getBuSlideView() {
			return buSlideView;
		}

		public void setBuSlideView(BuSlideView buSlideView) {
			this.buSlideView = buSlideView;
		}

	}

}