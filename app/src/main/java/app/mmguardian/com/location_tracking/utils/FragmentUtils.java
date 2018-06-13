package app.mmguardian.com.location_tracking.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import app.mmguardian.com.location_tracking.R;

/**
 * Control Fragment enter from where, like left or right
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class FragmentUtils {
	public static final String TAG = "FragmentUtils";
	public static final int TRANSITION_NULL = 0;
	public static final int TRANSITION_DEFAULT = 1;
	public static final int TRANSITION_LEFT_RIGHT = 2;
	public static final int TRANSITION_UP_DOWN = 3;
	public static final int TRANSITION_DOWN_UP = 4;


	private static FragmentTransaction setCustomAnimation(FragmentTransaction ft, int transition_flag) {
		if (transition_flag == TRANSITION_NULL) {
			ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		} else if (transition_flag == TRANSITION_LEFT_RIGHT) {
			ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
					R.anim.exit_to_right);
		} else if (transition_flag == TRANSITION_UP_DOWN) {
			ft.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_down, R.anim.enter_from_down,
					R.anim.exit_to_down);
		} else if (transition_flag == TRANSITION_DOWN_UP) {
			ft.setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_up);
		} else {
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}

		return ft;
	}

	public static void removeFragment(FragmentManager fm, Fragment fragment, int transition_flag) {
		FragmentTransaction ft = fm.beginTransaction();
		setCustomAnimation(ft, transition_flag);
		ft.remove(fragment);
		ft.commit();
	}

	public static void removeFragmentByTag(FragmentManager fm, String tag, int transition_flag) {
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		setCustomAnimation(fragmentTransaction, transition_flag);
		fragmentTransaction.remove(fm.findFragmentByTag(tag)).commit();
	}

	public static void addFragment(FragmentManager fm, Fragment fragment, int resId, boolean backStack,
                                   int transition_flag) {
		FragmentTransaction ft = fm.beginTransaction();
		setCustomAnimation(ft, transition_flag);
		ft.add(resId, fragment, fragment.getClass().getName());
		if (backStack) {
			ft.addToBackStack(fragment.getClass().getName());
		}
		ft.commitAllowingStateLoss();
	}

	public static void replaceFragment(FragmentManager fm, Fragment fragment, int resId, boolean backStack,
                                       int transition_flag) {
		//fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		
		FragmentTransaction ft = fm.beginTransaction();
		setCustomAnimation(ft, transition_flag);

		ft.replace(resId, fragment, fragment.getClass().getName());
		if (backStack) {
			ft.addToBackStack(fragment.getClass().getName());
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * This method will go check all the back stacks of the added fragments and their nested fragments
	 * to the the {@code FragmentManager} passed as parameter.
	 * If there is a fragment and the back stack of this fragment is not empty,
	 * then emulate 'onBackPressed' behaviour, because in default, it is not working.
	 *
	 * @param fm the fragment manager to which we will try to dispatch the back pressed event.
	 * @return {@code true} if the onBackPressed event was consumed by a child fragment, otherwise {@code false}.
	 */
	public static boolean dispatchOnBackPressedToFragments(FragmentManager fm) {

		List<Fragment> fragments = fm.getFragments();
		boolean result;
		if (fragments != null && !fragments.isEmpty()) {
			for (Fragment frag : fragments) {
				if (frag != null && frag.isAdded() && frag.getChildFragmentManager() != null) {
					// go to the next level of child fragments.
					result = dispatchOnBackPressedToFragments(frag.getChildFragmentManager());
					if (result) return true;
				}
			}
		}

		// if the back stack is not empty then we pop the last transaction.
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			fm.executePendingTransactions();
			return true;
		}

		return false;
	}
}
