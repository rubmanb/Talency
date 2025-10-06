export interface Employee {
  active: boolean;
  contractExpireDate: string;
  dateOfBirth: string;
  lastName: string;
  firstName: string;
  id?: number;
  dni: string;
  address?: string;
  city?: string;
  country?: string;
  phone?: number;
  position: string;
  gender?: string;
  health_insurance_number: string;
  marital_status: string;
  email_personal?: string;
  emergency_contact?: string;
  job_level?: string;
  work_schedule?: string;
  seniority?: string;
  vacations_days_allocated?: number;
  vacations_days_used?: number;
  bank_name?: string;
  departmentName?: string;
  contract_type?: string;
  contract_expire_date?: string;
  hireDate: string;
  fireDate?: string;
  iban?: string; // dato sensible
}
