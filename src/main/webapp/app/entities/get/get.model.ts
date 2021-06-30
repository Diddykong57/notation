import { IAssessment } from 'app/entities/assessment/assessment.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IGet {
  id?: number;
  note?: number;
  assessment?: IAssessment;
  student?: IStudent;
}

export class Get implements IGet {
  constructor(public id?: number, public note?: number, public assessment?: IAssessment, public student?: IStudent) {}
}

export function getGetIdentifier(get: IGet): number | undefined {
  return get.id;
}
