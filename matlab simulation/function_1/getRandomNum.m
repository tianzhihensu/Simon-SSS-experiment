function node_value_info = getRandomNum( node_info, num_monitor )
%GETRANDOMNUM 此处显示有关此函数的摘要
% function node_value_info = getRandomNum( node_info, num_monitor ):
% generate a node list that monitors has been assigned.
% 
% Input:
%       node_info: the initilized nodes list, num_node rows by 4 cols
%       num_monitor: number of monitors
%   
% Output
%       node_value_info: num_node rows by 3 cols
%       node_value_info(:, 1): the value of node
%       node_value_info(:, 2): the MU of node delay
%       node_value_info(:, 3): the SIGMA of node delay
%       node_value_info(:, 4): the normal value of node
node_value_info = node_info;   % initilize the return

% generate num_monitor rand nums from series 1, 2, ... , k, corresponding to the numbers of nodes
rows_cols_node_info = size(node_info);
num_node = rows_cols_node_info(1, 1);

% the following method is low-efficient when the numbers of monitors is
% large
%{
while 1
    value_node = round(rand(num_monitor, 1) * (num_node - 1) + 1);     % generate a num_monitor by 1 matrix, which will change the first col of node_value_node
                                                % num_node is the range
    temp = unique(value_node);
    if length(temp(:, 1)) == num_monitor             % when the condition that elements in value_nodes are unique is satisfied, get out of while loop
        break;
    end
end
%}

% a new method that select some serial numbers randomly.
count = 0;
value_node = zeros(num_monitor, 1); % store the selected serial number
temp = [1:num_node];
while count < num_monitor
    indexTemp = round(rand(1, 1) * (num_node - 1 - count) + 1);
    value_node(count + 1, 1) = temp(1, indexTemp);
    temp(:, indexTemp) = [];
    count  = count + 1;
end

if num_monitor > 0      % execute for loops while num_monitor is greater than zero
    for i=1:num_monitor
        index = value_node(i, 1);
        node_value_info(index, 1) = node_info(index, 4);    % set the selected node to its normal
    end
end
end

