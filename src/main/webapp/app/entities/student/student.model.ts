import { IGet } from 'app/entities/get/get.model';
import { IDegree } from 'app/entities/degree/degree.model';

export interface IStudent {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  gets?: IGet[] | null;
  degree?: IDegree;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public gets?: IGet[] | null,
    public degree?: IDegree
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
