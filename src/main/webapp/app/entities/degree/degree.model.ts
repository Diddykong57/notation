import { IStudent } from 'app/entities/student/student.model';
import { ISubject } from 'app/entities/subject/subject.model';

export interface IDegree {
  id?: number;
  nameDipl?: string | null;
  students?: IStudent[] | null;
  subjects?: ISubject[] | null;
}

export class Degree implements IDegree {
  constructor(
    public id?: number,
    public nameDipl?: string | null,
    public students?: IStudent[] | null,
    public subjects?: ISubject[] | null
  ) {}
}

export function getDegreeIdentifier(degree: IDegree): number | undefined {
  return degree.id;
}
