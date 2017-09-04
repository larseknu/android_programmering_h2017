package no.hiof.larseknu.playingwithfragments.fragmenttabsexample;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import no.hiof.larseknu.playingwithfragments.R;
import no.hiof.larseknu.playingwithfragments.adapter.MovieRecyclerAdapter;
import no.hiof.larseknu.playingwithfragments.adapter.RecycleAdapterListener;
import no.hiof.larseknu.playingwithfragments.model.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionFragment extends Fragment implements RecycleAdapterListener {

    private RecyclerView recyclerView;
    private OnMovieFragmentInteractionListener listener;

    public CollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        setUpRecyclerView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            listener = (OnMovieFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
    }

    private void setUpRecyclerView(View view) {

        recyclerView = view.findViewById(R.id.collectionRecyclerView);
        MovieRecyclerAdapter adapter = new MovieRecyclerAdapter(getContext(), Movie.getData(), this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // (Context context, int spanCount)
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    @Override
    public void movieSelected(Movie movie) {
        Toast.makeText(getContext(), movie.getTitle() + " Selected", Toast.LENGTH_SHORT).show();

        listener.onMovieSelected(movie);
    }

    public interface OnMovieFragmentInteractionListener {
        void onMovieSelected(Movie movie);
    }
}
