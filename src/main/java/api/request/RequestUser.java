package api.request;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class RequestUser {
    private String firstname;
    private String lastname;
    private String additionalneeds;
    private Bookingdates bookingdates;
    private int totalprice;
    private boolean depositpaid;


	//Manual Builder part
    private RequestUser(Builder builder) {
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.additionalneeds = builder.additionalneeds;
        this.bookingdates = builder.bookingdates;
        this.totalprice = builder.totalprice;
        this.depositpaid = builder.depositpaid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public Bookingdates getBookingdates() {
    	return bookingdates;
	}
    public int getTotalprice() {
        return totalprice;
    }

    public boolean getDepositpaid() {
        return depositpaid;
    }

    public static class Builder {
        private String firstname;
        private String lastname;
        private String additionalneeds;
        private Bookingdates bookingdates;
        private int totalprice;
        private boolean depositpaid;

        public Builder firstname(final String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(final String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder additionalneeds(final String additionalneeds) {
            this.additionalneeds = additionalneeds;
            return this;
        }

        public Builder bookingdates(final Bookingdates bookingdates) {
        	this.bookingdates = bookingdates;
        	return this;
		}

        public Builder totalprice(final int totalprice) {
        	this.totalprice = totalprice;
        	return this;
		}

		public Builder depositpaid(final boolean depositpaid) {
        	this.depositpaid = depositpaid;
        	return this;
		}

		public RequestUser build() {
        	return new RequestUser(this);
		}
    }
}
