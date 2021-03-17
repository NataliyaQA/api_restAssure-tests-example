package api.request;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Bookingdates{
	private String checkin;
	private String checkout;


//Manual Builder part
	private Bookingdates(Builder builder) {
		this.checkin = builder.checkin;
		this.checkout = builder.checkout;
	}

	public String getCheckin(){
		return checkin;
	}

	public String getCheckout(){
		return checkout;
	}

	public static class Builder{
		private String checkin;
		private String checkout;

		public Builder checkin(final String checkin){
			this.checkin = checkin;
			return this;
		}

		public Builder checkout(final String checkout){
			this.checkout = checkout; //may I write only "return this" instead of 2 separate strings (37, 38)?
			return this;
		}

		public Bookingdates build(){
			return new Bookingdates(this);
		}
	}
}
