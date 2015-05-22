function [ value_node_result ] = setMoniToHiPriNode( value_node, temp_higher, len_higher )
%SETMONTO 此处显示有关此函数的摘要
% function [ value_node ] = setMoniToHiPriNode( value_node, len_higher ):generate a
% value_node matrix with 1 cols.
% The function name setMoniToHiPriNode means set monitors to higher
% priority nodes.
%
% Input:
%       value_node: a matrix that store random serial number.
%       temp_higher: a set of nodes with higher priority.
%       len_higher: the length of higher priority set.
%
% Output:
%       value_node_result: an incomplete matrix that has stored serial numbers of
%       higher priority nodes.

value_node_result = value_node;
for i=1:len_higher
    value_node_result(i, 1) = temp_higher(1, i);
end

end

