package app.psychic.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import app.psychic.R;
import app.psychic.databinding.ViewDoctorItemBinding;
import app.psychic.models.Doctor;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Doctor> doctors;

    public DoctorAdapter(Context context) {
        this.context = context;
        this.doctors = new ArrayList<>();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors.clear();
        this.doctors.addAll(doctors);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDoctorItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_doctor_item, parent, false);
        return new DoctorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position) {
        holder.binding.name.setText(doctors.get(position).getName());
        holder.binding.location.setText(doctors.get(position).getLocation());

        holder.binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + doctors.get(holder.getAdapterPosition()).getContact()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ViewDoctorItemBinding binding;

        public DoctorViewHolder(ViewDoctorItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
