import { ProfileResponse } from './profile-response';

export interface IdentityResponse {
  id: string;
  username: string;
  profile: ProfileResponse;
}
