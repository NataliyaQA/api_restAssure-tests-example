package api.response;

import api.request.Bookingdates;
import api.request.RequestUser;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class ResponseUser{ // do we use builder here when annotation Builder is not commented?
	// Or there is no reason to build something for response?
	private RequestUser booking;
	private int bookingid;
}
